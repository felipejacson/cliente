package com.cliente.controller;

import com.cliente.util.Path;
import com.cliente.util.StandardResponse;
import com.cliente.util.StatusResponse;
import com.skip.dao.consumidor.ConsumidorDAOImpl;
import com.skip.dao.model.Consumidor;
import com.skip.dao.model.Consumidor;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.Optional;

import static com.cliente.ClienteApp.gson;

public class ConsumidorController {

    static ConsumidorDAOImpl productDAO = new ConsumidorDAOImpl();

    public static Route getAllConsumidores = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        Collection<Consumidor> list = productDAO.select(new Consumidor());

        StandardResponse ret = null;
        if(list.size() > 0) {
            response.status(200);
            ret = new StandardResponse(StatusResponse.OK, gson.toJsonTree(list));
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "Empty list!");
        }

        return gson.toJson(ret);
    };

    public static Route getConsumidorById = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Consumidor> p = productDAO.getConsumidorById(id);

                if (p.isPresent()) {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.OK, gson.toJsonTree(p.get()));
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Consumidor not found.");
                }
            } catch (NumberFormatException e) {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The param must be a number.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The param must be entered.");
        }

        return gson.toJson(ret);
    };

    public static Route deleteConsumidor = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Consumidor> p = productDAO.getConsumidorById(id);

                if (p.isPresent()) {
                    if (productDAO.delete(p.get().getId())) {
                        response.status(200);
                        ret = new StandardResponse(StatusResponse.OK, "Consumidor deleted.");
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The product has not been deleted.");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Consumidor not found.");
                }
            } catch (NumberFormatException e) {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The param must be a number.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The param must be entered.");
        }

        return gson.toJson(ret);
    };

    public static Route putConsumidor = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Consumidor> p = productDAO.getConsumidorById(id);

                if (p.isPresent()) {
                    Consumidor consumidor = p.get();
                    consumidor.setNome(request.queryParams("nome"));
                    consumidor.setEmail(request.queryParams("email"));
                    if (productDAO.update(consumidor)) {
                        response.status(200);
                        ret = new StandardResponse(StatusResponse.OK, "Consumidor updated.");
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The product has not been updated.");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Consumidor not found.");
                }
            } catch (Exception e) {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The param must be a number.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The param must be entered.");
        }

        return gson.toJson(ret);
    };

    public static Route postConsumidor = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> name = Optional.ofNullable(request.queryParams("nome"));
        Optional<String> email = Optional.ofNullable(request.queryParams("email"));
        if (name.isPresent() && email.isPresent()) {
            Consumidor consumidor = new Consumidor();
            consumidor.setNome(name.get());
            consumidor.setEmail(email.get());

            if (productDAO.insert(consumidor)) {
                ret = new StandardResponse(StatusResponse.OK, "Consumidor inserted.");
                response.status(201);
            } else {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The product has not been inserted.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The params must be entered.");
        }

        return gson.toJson(ret);
    };

}
