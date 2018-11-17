<!DOCTYPE html>

<html lang="en">
<head>
    <title>file provide preview services</title>
    <link rel="stylesheet" href="../static/css/viewer.min.css" />
    <link rel="stylesheet" href="../static/css/loading.css" />
    <link rel="stylesheet" href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
    <link rel="stylesheet" href="../static/bootstrap-table-1.11.1/dist/bootstrap-table.css" />
    <link rel="stylesheet" href="../static/bootstrap-fileinput/css/fileinput.css" />
    <link rel="stylesheet" href="../static/bootstrap-fileinput/themes/explorer/theme.css" />
    <style type="text/css">
    </style>
</head>

<body>
<div class="panel-group" id="accordion">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion"
                   href="#collapseTwo">
                    文件预览
                </a>
            </h4>
        </div>
        <div id="collapseTwo" class="panel-collapse collapse">
            <div class="panel-body">
                <div style="padding: 10px">
                    <input type="file" id="upfile" name="upfile" class="file-loading" multiple="multiple">
                    <#--<form enctype="multipart/form-data" id="fileUpload">
                        <input type="file" name="file" />
                        <input type="button" id="btnsubmit" value=" 上 传 " />
                    </form>-->
                </div>
                <div>
                    <table id="table" data-pagination="true"></table>
                </div>
            </div>
        </div>
    </div>
    <div class="panel">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion"
                   href="#collapseThree">
                    更新记录
                </a>
            </h4>
        </div>
    </div>
    <div id="collapseThree" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="container">
			<pre style="font-size: 18px;" >
				2018年11月--C01 @author yanting: <br>
				<mark>1.自定义资源映射路径,访问静态资源--static/** ;</mark><br>
				<mark>2.Dependency Spring Data JPA Integration Hibernate ;</mark><br><br>
			</pre>
            </div>
        </div>
    </div>
</div>

<div class="loading_container">
    <div class="spinner">
        <div class="spinner-container container1">
            <div class="circle1"></div>
            <div class="circle2"></div>
            <div class="circle3"></div>
            <div class="circle4"></div>
        </div>
        <div class="spinner-container container2">
            <div class="circle1"></div>
            <div class="circle2"></div>
            <div class="circle3"></div>
            <div class="circle4"></div>
        </div>
        <div class="spinner-container container3">
            <div class="circle1"></div>
            <div class="circle2"></div>
            <div class="circle3"></div>
            <div class="circle4"></div>
        </div>
    </div>
</div>
<script src="../static/js/jquery-3.0.0.min.js" type="text/javascript"></script>
<script src="../static/jquery-form/dist/jquery.form.min.js" type="text/javascript"></script>
<script src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="../static/bootstrap-fileinput/js/fileinput.min.js"></script>
<script src="../static/bootstrap-table-1.11.1/dist/bootstrap-table.js"></script>
<script src="../static/bootstrap-fileinput/js/locales/zh.js"></script>
<script src="../static/bootstrap-fileinput/themes/explorer/theme.js"></script>
<script>
    function deleteFile(fileName) {
        $.ajax({
            url: '${baseUrl}deleteFile?fileName=' + encodeURIComponent(fileName),
            success: function (data) {
                data = eval("("+data+")");
                // 删除完成，刷新table
                if (0 == data.code) {
                    alert(data.msg);
                    $('#table').bootstrapTable('refresh', {});
                }else{
                    alert("del failure");
                }
            },
            error: function (data) {
                console.log(data);
            }
        })
    }
    $(function () {
        $('#upfile').fileinput({
            language: 'zh',
            uploadUrl: '${baseUrl}fileUpload',
            allowedFileExtensions : ["pdf","jpg","jpeg","doc","docx","xls","xlsx","ppt","pptx","txt"],/*上传文件格式限制*/
            showCaption: true,
            uploadAsync: true, //默认异步上传
            showUpload: true,
            showRemove: true,
            showClose: true,
            minFileCount: 1,
            maxFileCount: 10,
            layoutTemplates:{
                actionDelete: ''
            },
            browseClass: 'btn btn-primary'
        }).on("fileuploaded", function(event, data) {
            if(data.response){
                $('#table').bootstrapTable('refresh', {});
            }
        }).on('fileerror', function(event, data, msg) {  //一个文件上传失败
            console.log('文件上传失败！'+msg);
        });

        $('#table').bootstrapTable({
            url: 'listFiles',
            columns: [{
                field: 'fileName',
                title: '文件名'
            }, {
                field: 'action',
                title: '操作'
            },]
        }).on('pre-body.bs.table', function (e,data) {
            // 每个data添加一列用来操作
            $(data).each(function (index, item) {
                item.action = "<a class='btn btn-default' target='_blank' href='${baseUrl}onlinePreview?url="
                        + encodeURIComponent('${baseUrl}' + item.fileName ) +"'>预览</a>" +
                        "<a class='btn btn-default' target='_blank' href='javascript:void(0);' onclick='deleteFile(\""+item.fileName+"\")'>删除</a>";
            });
            return data;
        }).on('post-body.bs.table', function (e,data) {
            return data;
        });

        /*function showLoadingDiv() {
            var height = window.document.documentElement.clientHeight - 1;
            $(".loading_container").css("height", height).show();
        }*/
    });
</script>
</body>
</html>