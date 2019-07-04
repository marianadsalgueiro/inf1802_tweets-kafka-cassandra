import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("tweets/consumer")
public class TweetConsumerResource {
     @Inject
     LifecycleManager manager;
    private static final Logger logger = LoggerFactory.getLogger(TweetConsumerResource.class.getName());

    /**
     * Inicia o serviço de consumo de Tweets
     * @return 200 OK no caso de successo ou 500 em caso de falha
     */
    @GET
    public Response start() {
        Response r = null;
        try {
            manager.start();
            r = Response.ok("Consumidor de Twitter iniciado")
                    .build();
        } catch (Exception ex) {
            logger.error("Erro na inicialização do serviço", ex);
            r = Response.serverError().build();
        }
        return r;
    }

    /**
     * Finaliza o serviço de Coleta de Tweets
     * @return 200 OK no caso de successo ou 500 em caso de falha
     */
    @DELETE
    public Response stop() {
        Response r = null;
        try {
            manager.stop();
            r = Response.ok("Consumidor de Twitter finalizado")
                    .build();
        } catch (Exception ex) {
            logger.error("Erro no parado do serviço", ex);
            r = Response.serverError().build();
        }
        return r;
    }
}