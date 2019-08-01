package sample.basic.rpcmock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sviolet.thistle.util.concurrent.ThreadPoolExecutorUtils;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class RpcPipeLine {

    private ExecutorService threadPool = ThreadPoolExecutorUtils.createFixed(5, "rpc-pipeline-%d");

    @Autowired
    private ProviderService service;

    public Map<String, Object> invoke(Map<String, Object> request) {
        Future<Map<String, Object>> future = threadPool.submit(() -> service.invoke(request));
        try {
            return future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
