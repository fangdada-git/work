
/**
 * 根据标签name获取对应value数组
 * @param tagName
 */
function getArarryByTagName(tagName) {
    var result = [];
    var tag = $("[name='" + tagName + "']");
    if (tag && tag.length > 0) {
        for (var i = 0; i < tag.length; i++) {
            var val = $(tag[i]).val();
            if (val && val.indexOf(",") >= 0) {
                for (var j = 0; j < val.split(",").length; j++) {
                    var str = val.split(",")[j];
                    if (str !== "") {
                        result.push(val.split(",")[j]);
                    }
                }
            } else {
                result.push(val);
            }
        }
    }
    return result;
}

/**
 * 显示品牌
 * @param _obj
 */
function showApplyBrand(_obj, readOnly) {
    var tempArray = [];
    $.each(_obj.checkedBrandNodes, function (i, _item) {
        var item = {};
        item.id = _item.id;
        item.name = _item.name;
        item.type = _item.name;
        item.tagName = "carstyleIds";
        item.on = false;
        item.tagValue = findStyleIds(item.id, _obj.checkedCarStyleNodes);
        tempArray[i] = item;
    });

    // 清空品牌标签  重新渲染勾选项
    $("#brands").children("li").remove();
    var checkbox = layui.checkbox;
    checkbox({
        elem: "#brands"
        , checkBox: false
        , delConfirm: false
        , width: 80
        , showDelbtn: !readOnly
        , nodes: tempArray
        , click: function (node) {
            node.on = false;
        }
        , del: function (node) {
            return true;
        }
    });

    // 取出所有车型被全选的 品牌  增加特殊标识
    var arr = _obj.checkedBrandChildCheckAllNodes;
    if (arr && arr.length > 0) {
        for (var i in arr) {
            $("#brands").find('li[id^="'+arr[i].id+'"]').addClass("isCheckAllChild");
        }
    }
    return true;
}
//根据品牌ID获取选中的车型
function findStyleIds(_brandId, _addStyles) {
    var styles = "";
    if (_addStyles) {
        $.each(_addStyles, function (i, v) {
            if (v.pid == _brandId) {
                styles += (v.pid + "_" + v.id + ",");
            }
        })
    }
    if (styles.indexOf(",") != -1) {
        styles = styles.substring(0, styles.lastIndexOf(','));
    }

    return styles;
}

/**
 * 获取选中的品牌车型数据集合
 */
function getCheckBrandCarStyle(dataType){
    var carStyleList = new Array();
    $("#brands li").each(function(){
        var isCheckAllChild = $(this).hasClass("isCheckAllChild");
        var checkName = $(this).attr("title");
        if(isCheckAllChild){
            var carStyleJson = {
                "dataType":dataType,
                "dataDesc":checkName,
                "dataPid":$(this).attr("id")
            };
            carStyleList.push(carStyleJson);
        }else{
            $(this).find("input[name='carstyleIds']").each(function(index,item){
                var carStyleIds = $(item).val().split(",");
                for (var i = 0; i < carStyleIds.length; i++) {
                    var carStyleIdInfo = carStyleIds[i];
                    var carStyleJson = {
                        "dataId":carStyleIdInfo.split("_")[1],
                        "dataType":dataType,
                        "dataDesc":checkName,
                        "dataPid":carStyleIdInfo.split("_")[0]
                    };
                    carStyleList.push(carStyleJson);
                }
            });
        }
    });
    return carStyleList;
}