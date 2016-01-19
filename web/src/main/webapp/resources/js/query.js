function queryItemLog() {
    var params = {
        "id" : $('#queryVal').val()
    };
    $.ajax({
        url: "/itemlog/search",
        data: params,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            if (data['success'] == "false" || typeof(data['errorMsg']) != 'undefined'){
                alert(data['errorMsg']);
                return;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert("查询失败");
        }
    });
}