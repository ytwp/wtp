package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.service.WtpUseRecordService;

/**
 * @author : weipeng
 * @date : 2020-11-06 10:01 下午
 */
@Permission
@CrossOrigin
@RestController
@RequestMapping("/wtpUseRecord")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpUseRecordController {

    private final WtpUseRecordService wtpUseRecordService;

}
