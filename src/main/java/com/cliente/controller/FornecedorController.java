package com.cliente.controller;

import com.cliente.util.Path;
import com.cliente.util.StandardResponse;
import com.cliente.util.StatusResponse;
import com.skip.dao.fornecedor.FornecedorDAOImpl;
import com.skip.dao.model.Fornecedor;
import com.skip.dao.model.Ticket;
import com.skip.dao.ticket.TicketDAOImpl;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collection;
import java.util.Optional;

import static com.cliente.ClienteApp.gson;

public class FornecedorController {

    static FornecedorDAOImpl FornecedorDAO = new FornecedorDAOImpl();
    static TicketDAOImpl ticketDAO = new TicketDAOImpl();

    public static Route getAllFornecedores = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        Collection<Fornecedor> list = FornecedorDAO.select(new Fornecedor());

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

    public static Route getFornecedorById = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Fornecedor> p = FornecedorDAO.getFornecedorById(id);

                if (p.isPresent()) {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.OK, gson.toJsonTree(p.get()));
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Fornecedor not found.");
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

    public static Route deleteFornecedor = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Fornecedor> p = FornecedorDAO.getFornecedorById(id);

                if (p.isPresent()) {
                    Ticket t = new Ticket();
                    t.setFornecedorId(p.get().getId());
                    if(ticketDAO.select(t).isEmpty()) {
                        if (FornecedorDAO.delete(p.get().getId())) {
                            response.status(200);
                            ret = new StandardResponse(StatusResponse.OK, "Fornecedor deleted.");
                        } else {
                            response.status(400);
                            ret = new StandardResponse(StatusResponse.ERROR, "The Fornecedor has not been deleted.");
                        }
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The Fornecedor is inserted in a ticket (" + p.get().getId() + ").");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Fornecedor not found.");
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

    public static Route putFornecedor = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> param = Optional.of(request.params("id"));
        if (param.isPresent()) {
            try {
                Long id = Long.valueOf(param.get());

                Optional<Fornecedor> p = FornecedorDAO.getFornecedorById(id);

                if (p.isPresent()) {
                    Fornecedor consumidor = p.get();
                    consumidor.setNome(request.queryParams("nome"));
                    consumidor.setRamo(request.queryParams("ramo"));
                    if (FornecedorDAO.update(consumidor)) {
                        response.status(200);
                        ret = new StandardResponse(StatusResponse.OK, "Fornecedor updated.");
                    } else {
                        response.status(400);
                        ret = new StandardResponse(StatusResponse.ERROR, "The Fornecedor has not been updated.");
                    }
                } else {
                    response.status(200);
                    ret = new StandardResponse(StatusResponse.ERROR, "Fornecedor not found.");
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

    public static Route postFornecedor = (Request request, Response response) -> {
        response.type(Path.CONTENT_TYPE_JSON);
        StandardResponse ret = null;

        Optional<String> name = Optional.ofNullable(request.queryParams("nome"));
        Optional<String> ramo = Optional.ofNullable(request.queryParams("ramo"));
        if (name.isPresent() && ramo.isPresent()) {
            Fornecedor fornecedor = new Fornecedor();
            fornecedor.setNome(name.get());
            fornecedor.setRamo(ramo.get());

            if (FornecedorDAO.insert(fornecedor)) {
                ret = new StandardResponse(StatusResponse.OK, "Fornecedor inserted.");
                response.status(201);
            } else {
                response.status(400);
                ret = new StandardResponse(StatusResponse.ERROR, "The fornecedor has not been inserted.");
            }
        } else {
            response.status(400);
            ret = new StandardResponse(StatusResponse.ERROR, "The params must be entered.");
        }

        return gson.toJson(ret);
    };

}
