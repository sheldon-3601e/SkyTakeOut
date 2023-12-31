package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation(("新增员工"))
    public Result<String> saveEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeService.saveEmployee(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO pageQueryDTO){

        log.info("分页查询，数据：{}",pageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(pageQueryDTO);

        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改员工帐号状态")
    public Result updateStatus(@PathVariable("status") Integer status,Long id) {
        log.info("修改员工帐号状态：{},{}",status,id);

        employeeService.updateStatus(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询员工信息byId")
    public Result<Employee> updateStatus(@PathVariable("id") Long id) {
        log.info("查询员工信息byId：{}",id);

        Employee employee = employeeService.selectEmployeeById(id);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation(value = "修改员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息：{}",employeeDTO);

        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }



}
