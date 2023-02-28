package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.springframework.web.bind.annotation.ResponseBody;

//import org.springframework.web.bind.annotation.ResponseBody;

@RestController
//@Controller
public class MenuHandler {
    @Autowired
    MenuService menuService;

//    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
//    获取整个树
    public ResultEntity<Menu> getWholeTreeNew(){
//        1. 查询全部的Menu对象
        List<Menu> menuList = menuService.getAll();

//        2. 声明一个变量存储找到的根节点
        Menu root= null;

//        3. 创建map对象存储id和menu对象的对应关系，便于查找父节点
        Map<Integer, Menu> menuMap = new HashMap<>();

//        4. 遍历menuList，填充map
        for (Menu menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }

//        5. 再次遍历menuList，查找根节点、组装父子节点
        for (Menu menu : menuList) {
//            1. 获取当前对象pid
            Integer pid = menu.getPid();

//            2. 检查pid是否为null
            if(pid == null) {
//                将当前对象赋给root
                root = menu;
                continue;
            }
//            如果pid不为null，说明当前节点有父节点，进行组装
            menuMap.get(pid).getChildren().add(menu);
        }
//        根节点包含了整个树形结构，返回根节点就是返回整个树
        return ResultEntity.successWithData(root);
    }

//    新增菜单
//    @ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

//    菜单按钮更新
//    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }
//    删除菜单
//    @ResponseBody
    @RequestMapping("menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id){
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }
}
