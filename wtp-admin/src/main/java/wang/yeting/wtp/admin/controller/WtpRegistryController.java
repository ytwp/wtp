package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.vo.WtpRegistryVo;
import wang.yeting.wtp.admin.service.WtpRegistryService;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-08-24 17:27
 */
@Permission
@CrossOrigin
@RestController
@RequestMapping("/wtpRegistry")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpRegistryController {

    private final WtpRegistryService wtpRegistryService;

    @GetMapping("/find")
    public Result<List<WtpRegistry>> find(WtpRegistryVo wtpRegistryVo) {
        List<WtpRegistry> wtpRegistryList = wtpRegistryService.findRegistry(wtpRegistryVo.getAppId(),wtpRegistryVo.getClusterId());
        return Result.success(wtpRegistryList);
    }

}
