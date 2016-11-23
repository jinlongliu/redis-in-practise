<#include "header.ftl">
<#setting number_format="0.0###">
<#--<div class="jumbotron">-->
<script>
    var sock = new WebSocket('ws://localhost:8080/chatOnline');
    sock.onopen = function() {
        console.log('open');
    };
    sock.onmessage = function(e) {
        console.log('message', e.data);
        $("#msgHistory").append(e.data + "\n");
        var scrollHeight = $("#msgHistory")[0].scrollHeight;
        $("#msgHistory").scrollTop(scrollHeight);
    };
    sock.onclose = function() {
        console.log('close');
    };

    $(function () {
        $(".btn-info").click(function () {
            var msg = $("#msgBox").val();
            sock.send(msg);
            $("#msgBox").val("");
        });

        $(".btn-danger").click(function () {
            sock.close();
        });

        $("#btnSendAll").click(function () {
            var msg = $("#msgBoxSendAll").val();
            $.post("/sendAll",
                    {message: msg},
                    function (data, status) {

                    });
            $("#msgBox").val("");
        });



    });

    //    sock.send('test');
    //    sock.close();
</script>
<div class="container">
    <div class="row">
        <div class="col-md-8">
            <div>
                <h1>聊天记录</h1>
                <textarea id="msgHistory" rows="8" readonly style="width: 100%;resize: none"></textarea>
            </div>

        </div>
        <div class="ol-md-4">
            <button class="btn btn-danger push-right"><span class="glyphicon glyphicon-log-out"></span> 退出</button>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <h4>输入框</h4>
            <textarea id="msgBox" rows="4" style="width: 100%;resize: none"  placeholder="在此输入消息"></textarea>
        </div>

    </div>
    <div class="row">
        <div class="col-md-8">
            <button class="btn btn-info pull-right">发送</button>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8">
            <h4>系统消息群发</h4>
            <textarea id="msgBoxSendAll" rows="4" style="width: 100%;resize: none"  placeholder="在此输入消息"></textarea>
        </div>

    </div>
    <div class="row">
        <div class="col-md-8">
            <button id="btnSendAll" class="btn btn-info pull-right">发送</button>
        </div>
    </div>
</div>
<#--</div>-->
<#include "footer.ftl">