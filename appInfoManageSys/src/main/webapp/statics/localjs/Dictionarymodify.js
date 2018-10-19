function  loadTypeCode(){
    $.ajax({
		type:"GET",//请求类型
		url:"/appManager/flatform/dataDictionary/loadTypeCode.json",//请求的url
		data:{typeCode:$("#typeCode").val()},//请求参数
		dataType:"json",//ajax接口（请求url）返回的数据类型
		success:function(data){//data：返回数据（json对象）
			if (data=="true"){
				$("#typeCodeForCheck").html("<span style=\"color: red\">该数据编码已存在！</span>");
				return false;
			}else{
                $("#typeCodeForCheck").html("<span style=\"color: green\">该数据编码可以使用！</span>");
				return true;
			}
		},
		error:function(data){//当访问时候，404，500 等非200的错误状态码
			return false;
		}
	});
}
function  loadTypeName(){
    $.ajax({
        type:"GET",//请求类型
        url:"/appManager/flatform/dataDictionary/loadTypeName.json",//请求的url
        data:{typeName:$("#typeName").val()},//请求参数
        dataType:"json",//ajax接口（请求url）返回的数据类型
        success:function(data){//data：返回数据（json对象）
            if (data=="true"){
                $("#typeNameForCheck").html("<span style=\"color: red\">该数据名称已存在！</span>");
                return false;
            }else{
                $("#typeNameForCheck").html("<span style=\"color: green\">该数据名称可以使用！</span>");
                return true;
            }
        },
        error:function(data){//当访问时候，404，500 等非200的错误状态码
            return false;
        }
    });
}
function  loadBackendUser(cl,BackendUser){
    $.ajax({
        type:"GET",//请求类型
        url:"getBackendlist.json",//请求的url
        data:{},//请求参数
        dataType:"json",//ajax接口（请求url）返回的数据类型
        success:function(data){//data：返回数据（json对象）
            $("#"+BackendUser).html("");
            var options = "<option value=\"\">--请选择--</option>";
            for(var i = 0; i < data.length; i++){
                if(cl != null && cl != undefined && data[i].id == cl ){
                    options += "<option selected=\"selected\" value=\""+data[i].id+"\" >"+data[i].userName+"</option>";
                }else{
                    options += "<option value=\""+data[i].id+"\">"+data[i].userName+"</option>";
                }
            }
            $("#"+BackendUser).html(options);
        },
        error:function(data){//当访问时候，404，500 等非200的错误状态码
            alert("加载分类列表失败！");
        }
    });
}

$(function() {
    // //动态加载所属平台列表
    // $.ajax({
    //     type: "GET",//请求类型
    //     url: "datadictionarylist.json",//请求的url
    //     data: {tcode: "APP_FLATFORM"},//请求参数
    //     dataType: "json",//ajax接口（请求url）返回的数据类型
    //     success: function (data) {//data：返回数据（json对象）
    //         var fid = $("#fid").val();
    //         $("#flatformId").html("");
    //         var options = "<option value=\"\">--请选择--</option>";
    //         for (var i = 0; i < data.length; i++) {
    //             if (fid != null && fid != undefined && data[i].valueId == fid) {
    //                 options += "<option selected=\"selected\" value=\"" + data[i].valueId + "\" >" + data[i].valueName + "</option>";
    //             } else {
    //                 options += "<option value=\"" + data[i].valueId + "\">" + data[i].valueName + "</option>";
    //             }
    //         }
    //         $("#flatformId").html(options);
    //     },
    //     error: function (data) {//当访问时候，404，500 等非200的错误状态码
    //         alert("加载平台列表失败！");
    //     }
    // });
    var cl1 = $("#u1").val();
    //动态加载一级分类列表
    loadBackendUser(cl1, "createdBy");
    $("#typeCode").blur(function () {
        if (loadTypeCode()==false){
            $(this).focus();
        }
    });
    $("#typeName").blur(function () {
        if (loadTypeName()==false){
            $(this).focus();
        }
    });
    $("#back").on("click",function(){
        window.location.href = "/appManager/flatform/dataDictionary/list";
    });
});
      
      
      