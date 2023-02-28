function generatePage() {
    var pageInfo = getPageInfoRemote();
    fillTableBody(pageInfo);
    $("#summaryBox").prop("checked",false);
}
function getPageInfoRemote() {
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "keyword": window.keyword,
            "pageNum": window.pageNum,
            "pageSize": window.pageSize
        },
        "async": false,
        "dataType": "json"
    });
    console.log(ajaxResult);
    var statusCode = ajaxResult.status;
    if(statusCode != 200) {
        layer.msg("失败！响应状态码=" + statusCode + " 说明信息=" + ajaxResult.statusText);
        return null;
    }
    var resultEntity = ajaxResult.responseJSON;
    var result = resultEntity.result;
    if(result == "FAILED"){
        layer.msg(resultEntity.message);
        return null;
    }
    return resultEntity.data;
}

function fillTableBody(pageInfo) {
    $("#rolePageBody").empty();
    $("#Pagination").empty();
    if(pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！ 没有查询到您搜索的数据！ </td></tr>");
        return ;
    }
    for(var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;
        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input id='" + roleId + "' class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>"+roleName+"</td>";
        var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class='glyphicon glyphicon-check'></i></button>";
        //通过button的id属性把roleId传递到button按钮的单击响应函数中
        var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>";
        var tr = "<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";
        $("#rolePageBody").append(tr);
    }
    generateNavigator(pageInfo);
}

function generateNavigator(pageInfo) {
    var totalRecord = pageInfo.total;
    var properties = {
            "num_edge_entries": 3,
            "num_display_entries": 5,
            "callback": paginationCallBack,
            "items_per_page": pageInfo.pageSize,
            "current_page": pageInfo.pageNum - 1,
            "prev_text": "上一页",
            "next_text": "下一页"
        }
    $("#Pagination").pagination(totalRecord, properties);
}

function paginationCallBack(pageIndex, jQuery) {
    window.pageNum = pageIndex + 1;
    generatePage();
    return false;
}

function showConfirmModal(roleArray) {
    $("#confirmModal").modal("show");
    $("#roleNameDiv").empty();
    window.roleIdArray = [];
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");
        var roleId = role.roleId;
        window.roleIdArray.push(roleId);
    }
}

function fillAuthTree() {
    //  1.发送ajax请求查询Auth数据
    var ajaxReturn = $.ajax({
        "url":"assign/get/all/auth.json",
        "type":"post",
        "dataType":"json",
        // 开启同步
        "async":false
    });
    if(ajaxReturn.status != 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + " 说明是" + ajaxReturn.statusText);
        return ;
    }
    //  从响应结果中获取Auth的json数据
    //  从服务器查到的list不需要组装成树形结构，由ZTree进行组装
    var authList = ajaxReturn.responseJSON.data;

    //  对ZTree进行设置的json对象
    var setting = {
        "data": {
            "simpleData": {
                //  开启生成简单json功能
                "enable": true,
                //  不用默认的pid作为父节点，而是categoryId
                "pIdKey": "categoryId"
            },
            "key": {
                //  使用title属性显示结点名称
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    };

    //  生成树形结构
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);

    //  获取Ztreeobj对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");

    //  默认把节点展开
    zTreeObj.expandAll(true);

    //  查询已分配的Auth的id组成的数组
    ajaxReturn = $.ajax({
        "url":"assign/get/assigned/auth/id/by/role/id.json",
        "type":"post",
        "data":{
            "roleId":window.roleId
        },
        "dataType":"json",
        "async":false
    });
    if(ajaxReturn.status != 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + " 说明是" + ajaxReturn.statusText);
        return ;
    }
    //  从响应结果中获取authIdArray
    var authIdArray = ajaxReturn.responseJSON.data;

    //  根据authIdArray把树形结构对应的结点勾上
    for(var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        //  1.根据id查询到树形结构中对应的结点
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        //  2.将checkNode设置为被勾选
        var checked = true;
        //  设置为不联动，避免把不勾选的选上
        var checkTypeFlag = false;
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}