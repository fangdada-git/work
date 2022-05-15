

/**************************************工具相关start*****************************************/
//json的length
function getJsonLength(jsonData){
    var jsonLength = 0;
    for(var item in jsonData){
        jsonLength++;
    }
    return jsonLength;
}

// 获取地址
function getObjectURL(file) {
    var url = null;
    if (window.createObjectURL != undefined) {
        // basic
        url = window.createObjectURL(file);
    } else if (window.URL != undefined) {
        // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) {
        // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}


//获取图片的Blob值
function getImageBlob(url, cb) {
    var xhr = new XMLHttpRequest();
    xhr.open("get", url, true);
    xhr.setRequestHeader('Access-Control-Allow-Origin','*');
    xhr.responseType = "blob";
    xhr.onload = function() {
        if (this.status == 200) {
            if(cb) cb(this.response);
        }
    };
    xhr.send();
}


function strTrim(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
/**************************************工具相关end*****************************************/