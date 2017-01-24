package org.seckill.web;

import io.swagger.annotations.*;
import org.seckill.dto.ExposerDto;
import org.seckill.dto.SeckillExecutionDto;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.http.SeckillResult;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Ryan-hou
 * @description:
 * @className: SeckillController
 * @date January 16,2017
 */
@Api(description = "秒杀接口", value = "Seckill")
@Controller //@Service @Component
@RequestMapping("/seckill") // url:模块/资源/{id}/细分 -> /seckill/list
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        // 获取列表页
        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("list", seckillList);
        // list.jsp + model = ModelAndView
        return "list"; // /WEB-INF/jsp/"list".jsp
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list"; //区分重定向
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    // ajax json
    @RequestMapping(value = "/{seckillId}/exposer",
        method = RequestMethod.POST,
        produces = {"application/json;charset=UTF-8"})
    @ApiOperation(
            value = "暴露秒杀地址",
            notes = "暴露秒杀地址信息"
            //httpMethod = "POST",
            //response = SeckillResult.class
    )
    @ApiResponses( {
            @ApiResponse(code = 404, message = "根据业务定制http404含义" )
    } )
    @ResponseBody
    public SeckillResult<ExposerDto> exposer(
            @ApiParam(required = true, name = "seckillId", value = "秒杀商品id") @PathVariable("seckillId") Long seckilllId) {
        SeckillResult<ExposerDto> result;
        try {
            ExposerDto exposerDto = seckillService.exportSeckillUrl(seckilllId);
            result = new SeckillResult<>(true, exposerDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecutionDto> execute(@PathVariable("seckillId") Long seckillId,
                                    @PathVariable("md5") String md5,
                                    @CookieValue(value = "killPhone", required = false) Long phone) {
        //springmvc valid
        if (phone == null) {
            return new SeckillResult<>(false, "未注册");
        }
        SeckillResult<SeckillExecutionDto> result;
        try {
            // 调用存储过程
            SeckillExecutionDto seckillExecutionDto = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            return new SeckillResult<>(true, seckillExecutionDto);
        } catch (RepeatKillException e) {
            SeckillExecutionDto seckillExecutionDto = new SeckillExecutionDto(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecutionDto);
        } catch (SeckillCloseException e) {
            SeckillExecutionDto seckillExecutionDto = new SeckillExecutionDto(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(true, seckillExecutionDto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecutionDto seckillExecutionDto = new SeckillExecutionDto(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(true, seckillExecutionDto);
        }
    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(
            value = "获取系统当前时间",
            httpMethod = "GET",
            response = SeckillResult.class,
            notes = "获取系统当前时间,秒杀时间以系统时间为准"
    )
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
