<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>Title</title>
    <%@include file="/WEB-INF/include-head.jsp" %>
    <%@include file="/WEB-INF/modal-menu-add.jsp"%>
    <%@include file="/WEB-INF/modal-menu-confirm.jsp"%>
    <%@include file="/WEB-INF/modal-menu-edit.jsp"%>
    <link rel="stylesheet" href="ztree/zTreeStyle.css"/>
    <script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="crowd/my-menu.js"></script>
    <script type="text/javascript">
        $(function () {
            generateTree();
            //  给“添加子结点”绑定单击响应函数
            $("#treeDemo").on("click", ".addBtn", function () {

                //  将当前节点的id作为新节点的pid保存到全局变量
                window.pid = this.id;
                //  显示模态框
                $("#menuAddModal").modal("show");
                return false;
            });

            //  添加子结点的模态框中的保存按钮绑定单击响应函数
            $("#menuSaveBtn").click(function () {
                //  收集表单项用户输入的数据
                var name = $.trim($("#menuAddModal [name=name]").val());
                var url = $.trim($("#menuAddModal [name=url]").val());
                //  单选按钮要定位到被选中的那一个
                var icon = $("#menuAddModal [name=icon]:checked").val();
                $.ajax({
                    "url": "menu/save.json",
                    "type": "post",
                    "data": {
                        "pid": window.pid,
                        "name": name,
                        "url": url,
                        "icon": icon
                    },
                    "async": false,
                    "dataType": "json",
                    "success": function (response) {
                        var result = response.result;
                        if(result=="SUCCESS"){
                            layer.msg("操作成功！");
                            // 重新加载树形结构
                            // 注意：要在确认服务器端完成保存操作，再进行页面刷新
                            generateTree();
                        }
                        if(result=="FAILED"){
                            layer.msg("操作失败！" + response.message);
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                $("#menuAddModal").modal("hide");
                $("#menuResetBtn").click();
            });

            // 给编辑按钮绑定单击响应函数
            $("#treeDemo").on("click", ".editBtn", function () {
                window.id = this.id;
                $("#menuEditModal").modal("show");

                // 1.获取zTreeObj对象
                var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

                // 根据id属性查询节点对象
                var key = "id";
                var value = window.id;
                // 获取当前节点对象
                var currentNode = zTreeObj.getNodeByParam(key, value);
                // 回显表单数据
                $("#menuEditModal [name=name]").val(currentNode.name);
                $("#menuEditModal [name=url]").val(currentNode.url);
                // 回显radio：
                // 被选中的radio的value属性可以组成一个数组，将这个数组设置为
                // radio，就能把对应的值选中
                $("#menuEditModal [name=icon]").val([currentNode.icon]);
                return false;
            });
            // 给模态框的更新按钮绑定单击响应函数
            $("#menuEditBtn").click(function () {
                // 收集表单数据
                var name = $.trim($("#menuEditModal [name=name]").val());
                var url = $.trim($("#menuEditModal [name=url]").val());
                var icon = $("#menuEditModal [name=icon]:checked").val();
                // 发送ajax请求
                $.ajax({
                    "url": "menu/update.json",
                    "type": "post",
                    "data": {
                        "id": window.id,
                        "name": name,
                        "url": url,
                        "icon": icon
                    },
                    "async": false,
                    "dataType": "json",
                    "success": function (response) {
                        var result = response.result;
                        if(result=="SUCCESS"){
                            layer.msg("操作成功！");
                            generateTree();

                        }
                        if(result=="FAILED"){
                            layer.msg("操作失败！" + response.message);
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                $("#menuEditModal").modal("hide");
            });
            // 给x按钮绑定单击响应函数
            $("#treeDemo").on("click", ".removeBtn", function () {
                window.id = this.id;
                $("#menuConfirmModal").modal("show");
                // 1.获取zTreeObj对象
                var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");

                // 根据id属性查询节点对象
                var key = "id";
                var value = window.id;
                // 获取当前节点对象
                var currentNode = zTreeObj.getNodeByParam(key, value);
                $("#removeNodeSpan").html("【<i class='"+currentNode.icon+"'></i>"+currentNode.name+"】");
                return false;
            });
            // 给模态框的ok按钮绑定单击响应函数
            $("#menuRemoveBtn").click(function () {
                $.ajax({
                    "url": "menu/remove.json",
                    "type": "post",
                    "data": {
                        "id": window.id
                    },
                    "async": false,
                    "dataType": "json",
                    "success": function (response) {
                        var result = response.result;
                        if(result=="SUCCESS"){
                            layer.msg("操作成功！");
                            generateTree();
                        }
                        if(result=="FAILED"){
                            layer.msg("操作失败！" + response.message);
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                $("#menuConfirmModal").modal("hide");
            });
        });
    </script>
</head>
<body>
<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表 <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>