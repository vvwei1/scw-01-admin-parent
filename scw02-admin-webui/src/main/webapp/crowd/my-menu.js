//  生成树形结构的函数
function generateTree() {
    $.ajax({
        "url": "menu/get/whole/tree.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            var result = response.result;
            if(result == "SUCCESS"){
                var setting = {
                    "view": {
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom": myAddHoverDom,
                        "removeHoverDom": myRemoveHoverDom,
                    },
                    "data": {
                        "key": {
                            "url": "xxx"
                        }
                    }
                };
                var zNodes = response.data;
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if(result == "FAILED"){
                layer.msg(response.message);
            }
        }
    });
}

function myRemoveHoverDom(treeId, treeNode) {
    var btnGroupId = treeNode.tId + "_btnGrp";
    $("#"+btnGroupId).remove();
}

function myAddHoverDom(treeId, treeNode) {
    var btnGroupId = treeNode.tId + "_btnGrp";
    if($("#"+btnGroupId).length > 0) {
        return ;
    }
    var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title=' 删 除 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='"+treeNode.id+"' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title=' 修 改 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
    var level = treeNode.level;
    var btnHtml = "";
    if(level == 0){
        //根节点只允许添加
        btnHtml = addBtn;
    } else if (level == 1){
        //分支节点允许 添加 删除
        btnHtml = addBtn + " " + editBtn;
        var length = treeNode.children.length;
        //如果没有子结点，只允许删除
        if(length == 0){
            btnHtml = btnHtml + " " + removeBtn;
        }
    //    叶子结点允许 修改 删除
    } else if (level == 2){
        btnHtml = editBtn + " " + removeBtn;
    }
    var anchorId = treeNode.tId + "_a";
    $("#"+anchorId).after("<span id='"+btnGroupId+"'>" + btnHtml + "</span>");
}

function myAddDiyDom(treeId, treeNode) {
    // console.log("treeId="+treeId);
    // console.log(treeNode);
    var spanId = treeNode.tId + "_ico";
    $("#"+spanId).removeClass().addClass(treeNode.icon);
}