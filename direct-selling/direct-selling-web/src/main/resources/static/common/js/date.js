function timeFormat(time) {
    if(time == undefined){
        return "";
    }
    var date = new Date(time + 8 * 3600 * 1000); // 增加8小时
    return date.toJSON().substr(0, 19).replace('T', ' ');
}

Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
var DateUtils = {
dataTimeFormatter: function(time, type) {
    var self = this;

    var date;
    if (time) {
        date = new Date((time + "").length <=10 ? (Number(time) * 1000) : Number(time));
    } else {
        date = new Date();
    }

    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var second = date.getSeconds();

    switch (type) {
        case 0: // 01-05
            return self.zerofill(month) + "-" + self.zerofill(day);
        case 1: // 11:12
            return self.zerofill(hours) + ":" + self.zerofill(minutes);
        case 2: // 2015-01-05
            return year + "-" + self.zerofill(month) + "-" + self.zerofill(day);
        case 3: // 2015-01-05 11:12
            return year + "-" + self.zerofill(month) + "-" + self.zerofill(day) + " " + self.zerofill(hours) + ":" + self.zerofill(minutes);
        case 4: // 01.05
            return self.zerofill(month) + "." + self.zerofill(day);
        case 5: // 01-05 11:12
            return self.zerofill(month) + "-" + self.zerofill(day) + " " + self.zerofill(hours) + ":" + self.zerofill(minutes);
        case 6: // 2015/01/05
            return year + "/" + self.zerofill(month) + "/" + self.zerofill(day);
        default: // 2015-01-05 11:12:13
            return year + "-" + self.zerofill(month) + "-" + self.zerofill(day) + " " + self.zerofill(hours) + ":" + self.zerofill(minutes) + ":" + self.zerofill(second);
    }
},zerofill: function (v) {
        return v >= 10 ? v : '0' + v;
    }
}