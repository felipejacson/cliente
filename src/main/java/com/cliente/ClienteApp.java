package com.cliente;

import com.cliente.controller.ConsumidorController;
import com.cliente.controller.FornecedorController;
import com.cliente.util.FilterController;
import com.cliente.util.Path;
import com.cliente.util.StandardResponse;
import com.cliente.util.StatusResponse;
import com.google.gson.Gson;

import static spark.Spark.*;
import static spark.Spark.get;
import static spark.Spark.post;

public class ClienteApp {

    public static Gson gson;

    public static void main(String[] args) {

        gson = new Gson();

        //port(8080);

        before("*", FilterController.slash);

        path(Path.CONSUMIDOR_CONTROLLER, () -> {
            get(Path.ALL, ConsumidorController.getAllConsumidores);
            get(Path.BY_ID, ConsumidorController.getConsumidorById);
            delete(Path.BY_ID, ConsumidorController.deleteConsumidor);
            put(Path.BY_ID, ConsumidorController.putConsumidor);
            post(Path.ADD, ConsumidorController.postConsumidor);
        });

        path(Path.FORNECEDOR_CONTROLLER, () -> {
            get(Path.ALL, FornecedorController.getAllFornecedores);
            get(Path.BY_ID, FornecedorController.getFornecedorById);
            delete(Path.BY_ID, FornecedorController.deleteFornecedor);
            put(Path.BY_ID, FornecedorController.putFornecedor);
            post(Path.ADD, FornecedorController.postFornecedor);
        });

        get("*", (request, response) -> {
            response.type(Path.CONTENT_TYPE_JSON);
            response.status(404);
            StandardResponse ret = new StandardResponse(StatusResponse.ERROR, "Not Found");

            return gson.toJson(ret);
        });

    }

}
