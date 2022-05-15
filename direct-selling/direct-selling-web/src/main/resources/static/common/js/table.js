/**
 * 判断是否有layui环境
 * @param elem
 * @param settings
 * @param layui
 */
function render(layui, elem, settings) {
    settings = settings || {};
    if (!layui || !layui.table || !layui.jquery) {
        // alert("请添加layui、layui.table、layui.jquery环境");
    }
    if (!elem) {
        // alert("请指定表格渲染dom");
    }
    return renderTab(layui, elem, settings);
}

/**
 * 表格点击单选方法
 */
function radioCheck() {
    layui.jquery(".layui-table-body .layui-form-radio").parents("tr").click(function (e) {
        var tclass = layui.jquery.trim(layui.jquery(this).find(".layui-form-radio")[0].className);
        if (tclass != layui.jquery.trim(e.target.className)) {
            layui.jquery(this).find(".layui-form-radio").click();
        }
        return true;
    })
};

/**
 * 渲染tab
 * @param elem
 * @param settings
 * @param layui
 */
function renderTab(layui, elem, settings) {
    var func = [];
    func.push(radioCheck);

    var defaults = {
        elem: elem,
        method: "post",
        page: true,
        limits: [1, 5, 10, 20, 50, 100],
        size: 'sm',
        parseData: function (data) {
            var ret = [];
            ret.data = data.data;
            ret.count = data.count;
            ret.code = data.code;
            return ret;
        },
        request: {
            pageName: 'page' //页码的参数名称，默认：page
            , limitName: 'limit' //每页数据量的参数名，默认：limit
        },
        done: function () {
            for (var i = 0; i < func.length; i++) {
                try {
                    func[i]();
                } catch (e) {
                    console.log(e);
                }
            }
            (typeof checkAuthButton) != "undefined" ? checkAuthButton() : null;
        }
    };
    if(settings.done){
        func.push(settings.done);
        delete settings.done;
    }
    var ops = layui.jquery.extend(defaults, settings);
    return layui.table.render(ops);
}


function refButton(thiz, $) {
    $(thiz).attr("disabled", true);
    var str = $(thiz).html();
    var len = str.length;
    var val1 = "稍等‹‹‹"
    $(thiz).html(val1).addClass("layui-btn-disabled");
    var n = window.setInterval(function () {
        if ($(thiz).html() == val1) {
            $(thiz).html("稍等›››")
        } else {
            $(thiz).html(val1)
        }
    }, 500);
    var m = window.setTimeout(function () {
        $(thiz).attr("disabled", false)
            .html(str)
            .removeClass("layui-btn-disabled");
        window.clearInterval(n);
    }, 9000);
    $(thiz).data("str", str);
    $(thiz).data("interval", n);
    $(thiz).data("timeout", m);
    return function () {
        cloButton(thiz, $);
    }
}

function cloButton(thiz, $) {
    $(thiz).attr("disabled", false)
        .html($(thiz).data("str"))
        .removeClass("layui-btn-disabled");
    window.clearInterval($(thiz).data("interval"));
    window.clearTimeout($(thiz).data("timeout"));
}