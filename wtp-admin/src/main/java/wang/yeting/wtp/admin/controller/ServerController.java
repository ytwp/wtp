package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.yeting.wtp.admin.service.ServerService;
import wang.yeting.wtp.core.biz.model.*;
import wang.yeting.wtp.core.util.HttpResponse;

/**
 * @author : weipeng
 * @date : 2020-07-23 23:21
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerController {

    private final ServerService serverService;

    @RequestMapping("/registry")
    public HttpResponse<ConfigEvent> registry(@RequestBody QueryBo queryBo) {
        ConfigEvent configEvent = serverService.registry(queryBo);
        return new HttpResponse<>(HttpResponse.SUCCESS_CODE, configEvent);
    }

    @RequestMapping("/destroy")
    public HttpResponse<Boolean> destroy(@RequestBody QueryBo queryBo) {
        Boolean destroy = serverService.destroy(queryBo);
        return new HttpResponse<>(HttpResponse.SUCCESS_CODE, destroy);
    }

    @RequestMapping("/pushLog")
    public HttpResponse<Boolean> pushLog(@RequestBody WtpLogBo wtpLogBo) {
        Boolean pushLog = serverService.pushLog(wtpLogBo);
        return new HttpResponse(HttpResponse.SUCCESS_CODE, pushLog);
    }

    @RequestMapping("/pullConfig/{appId}/{clusterId}/{ip}")
    public HttpResponse<ConfigChangeEvent> pullConfig(@PathVariable("appId") String appId, @PathVariable("clusterId") String clusterId, @PathVariable("ip") String ip) {
        ConfigChangeEvent configChangeEvent = serverService.pullConfig(appId, clusterId, ip);
        return new HttpResponse(HttpResponse.SUCCESS_CODE, configChangeEvent);
    }

    @RequestMapping("/registerNoConfigurationWtp")
    public HttpResponse<Boolean> registerNoConfigurationWtp(@RequestBody WtpBo wtpBo) {
        Boolean registerNoConfigurationWtp = serverService.registerNoConfigurationWtp(wtpBo);
        return new HttpResponse(HttpResponse.SUCCESS_CODE, registerNoConfigurationWtp);
    }

    @RequestMapping("/taskPullConfig/{appId}/{clusterId}/{ip}")
    public HttpResponse<ConfigEvent> taskPullConfig(@PathVariable("appId") String appId, @PathVariable("clusterId") String clusterId, @PathVariable("ip") String ip) {
        ConfigEvent configEvent = serverService.taskPullConfig(appId, clusterId, ip);
        return new HttpResponse(HttpResponse.SUCCESS_CODE, configEvent);
    }
}
