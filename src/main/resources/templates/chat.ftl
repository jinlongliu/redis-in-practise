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
        $("#msgHistory").append("Tom:\n&nbsp;&nbsp;&nbsp;&nbsp;" + e.data + "\n");
        var scrollHeight = $("#msgHistory")[0].scrollHeight;
        $("#msgHistory").scrollTop(scrollHeight);
    };
    sock.onclose = function() {
        console.log('close');
    };

    $(function () {
        $(".btn").click(function () {
            var msg = $("#msgBox").val();
            sock.send(msg);
        });
    });

    //    sock.send('test');
    //    sock.close();
</script>
<div class="container">
    <div class="row">
        <div class="col-md-8">
            <h1>聊天记录</h1>
            <textarea id="msgHistory" rows="8" readonly style="width: 100%;resize: none"></textarea>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <h4>输入框</h4>
            <textarea id="msgBox" rows="4" style="width: 100%;resize: none"  placeholder="在此输入消息"></textarea>
        </div>
        <div class="col-md-4">

        </div>
    </div>
    <div class="row">
        <div class="col-md-8">
            <button class="btn btn-info pull-right">发送</button>
        </div>
    </div>
</div>
<#--</div>-->
<#include "footer.ftl">