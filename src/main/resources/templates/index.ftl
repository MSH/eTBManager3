<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>e-TB Manager</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
</head>

<style type="text/css">
    .cssload-loading {
        position: absolute;
        left: 50%;
        width: 33px;
        height: 33px;
        margin: -17px 0 0 -17px;
    }

    .cssload-loading i {
        position: absolute;
        top: 33px; left: 33px;
        display: block;
        width: 33px;
        height: 33px;
        background: rgb(101,199,121);
        border-radius: 33px;
        animation: cssload-spin2 2.63s ease-in-out infinite;
        -o-animation: cssload-spin2 2.63s ease-in-out infinite;
        -ms-animation: cssload-spin2 2.63s ease-in-out infinite;
        -webkit-animation: cssload-spin2 2.63s ease-in-out infinite;
        -moz-animation: cssload-spin2 2.63s ease-in-out infinite;
    }
    .cssload-loading i:first-child {
        top: -33px; left: 0;
        background: rgb(20,168,109);
        animation: cssload-spin 2.63s ease-in-out infinite;
        -o-animation: cssload-spin 2.63s ease-in-out infinite;
        -ms-animation: cssload-spin 2.63s ease-in-out infinite;
        -webkit-animation: cssload-spin 2.63s ease-in-out infinite;
        -moz-animation: cssload-spin 2.63s ease-in-out infinite;
    }
    .cssload-loading i:last-child {
        top: 33px; left: -33px;
        background: rgb(79,133,29);
        animation: cssload-spin3 2.63s ease-in-out infinite;
        -o-animation: cssload-spin3 2.63s ease-in-out infinite;
        -ms-animation: cssload-spin3 2.63s ease-in-out infinite;
        -webkit-animation: cssload-spin3 2.63s ease-in-out infinite;
        -moz-animation: cssload-spin3 2.63s ease-in-out infinite;
    }






    @keyframes cssload-spin {
        0% {
            top: -33px;
            left: 0;
            transform: scale(1);
        }
        17% {
            transform: scale(.5);
        }
        33% {
            top: 33px;
            left: 33px;
            transform: scale(1);
        }
        50% {
            transform: scale(.5);
        }
        66% {
            top: 33px;
            left: -33px;
            transform: scale(1);
        }
        83% {
            transform: scale(.5);
        }
        100% {
            top: -33px;
            left: 0;
            transform: scale(1);
        }
    }

    @-o-keyframes cssload-spin {
        0% {
            top: -33px;
            left: 0;
            -o-transform: scale(1);
        }
        17% {
            -o-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: 33px;
            -o-transform: scale(1);
        }
        50% {
            -o-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: -33px;
            -o-transform: scale(1);
        }
        83% {
            -o-transform: scale(.5);
        }
        100% {
            top: -33px;
            left: 0;
            -o-transform: scale(1);
        }
    }

    @-ms-keyframes cssload-spin {
        0% {
            top: -33px;
            left: 0;
            -ms-transform: scale(1);
        }
        17% {
            -ms-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: 33px;
            -ms-transform: scale(1);
        }
        50% {
            -ms-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: -33px;
            -ms-transform: scale(1);
        }
        83% {
            -ms-transform: scale(.5);
        }
        100% {
            top: -33px;
            left: 0;
            -ms-transform: scale(1);
        }
    }

    @-webkit-keyframes cssload-spin {
        0% {
            top: -33px;
            left: 0;
            -webkit-transform: scale(1);
        }
        17% {
            -webkit-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: 33px;
            -webkit-transform: scale(1);
        }
        50% {
            -webkit-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: -33px;
            -webkit-transform: scale(1);
        }
        83% {
            -webkit-transform: scale(.5);
        }
        100% {
            top: -33px;
            left: 0;
            -webkit-transform: scale(1);
        }
    }

    @-moz-keyframes cssload-spin {
        0% {
            top: -33px;
            left: 0;
            -moz-transform: scale(1);
        }
        17% {
            -moz-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: 33px;
            -moz-transform: scale(1);
        }
        50% {
            -moz-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: -33px;
            -moz-transform: scale(1);
        }
        83% {
            -moz-transform: scale(.5);
        }
        100% {
            top: -33px;
            left: 0;
            -moz-transform: scale(1);
        }
    }

    @keyframes cssload-spin2 {
        0% {
            top: 33px;
            left: 33px;
            transform: scale(1);
        }
        17% {
            transform: scale(.5);
        }
        33% {
            top: 33px;
            left: -33px;
            transform: scale(1);
        }
        50% {
            transform: scale(.5);
        }
        66% {
            top: -33px;
            left: 0;
            transform: scale(1);
        }
        83% {
            transform: scale(.5);
        }
        100% {
            top: 33px;
            left: 33px;
            transform: scale(1);
        }
    }

    @-o-keyframes cssload-spin2 {
        0% {
            top: 33px;
            left: 33px;
            -o-transform: scale(1);
        }
        17% {
            -o-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: -33px;
            -o-transform: scale(1);
        }
        50% {
            -o-transform: scale(.5);
        }
        66% {
            top: -33px;
            left: 0;
            -o-transform: scale(1);
        }
        83% {
            -o-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: 33px;
            -o-transform: scale(1);
        }
    }

    @-ms-keyframes cssload-spin2 {
        0% {
            top: 33px;
            left: 33px;
            -ms-transform: scale(1);
        }
        17% {
            -ms-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: -33px;
            -ms-transform: scale(1);
        }
        50% {
            -ms-transform: scale(.5);
        }
        66% {
            top: -33px;
            left: 0;
            -ms-transform: scale(1);
        }
        83% {
            -ms-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: 33px;
            -ms-transform: scale(1);
        }
    }

    @-webkit-keyframes cssload-spin2 {
        0% {
            top: 33px;
            left: 33px;
            -webkit-transform: scale(1);
        }
        17% {
            -webkit-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: -33px;
            -webkit-transform: scale(1);
        }
        50% {
            -webkit-transform: scale(.5);
        }
        66% {
            top: -33px;
            left: 0;
            -webkit-transform: scale(1);
        }
        83% {
            -webkit-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: 33px;
            -webkit-transform: scale(1);
        }
    }

    @-moz-keyframes cssload-spin2 {
        0% {
            top: 33px;
            left: 33px;
            -moz-transform: scale(1);
        }
        17% {
            -moz-transform: scale(.5);
        }
        33% {
            top: 33px;
            left: -33px;
            -moz-transform: scale(1);
        }
        50% {
            -moz-transform: scale(.5);
        }
        66% {
            top: -33px;
            left: 0;
            -moz-transform: scale(1);
        }
        83% {
            -moz-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: 33px;
            -moz-transform: scale(1);
        }
    }

    @keyframes cssload-spin3 {
        0% {
            top: 33px;
            left: -33px;
            transform: scale(1);
        }
        17% {
            transform: scale(.5);
        }
        33% {
            top: -33px;
            left: 0;
            transform: scale(1);
        }
        50% {
            transform: scale(.5);
        }
        66% {
            top: 33px;
            left: 33px;
            transform: scale(1);
        }
        83% {
            transform: scale(.5);
        }
        100% {
            top: 33px;
            left: -33px;
            transform: scale(1);
        }
    }

    @-o-keyframes cssload-spin3 {
        0% {
            top: 33px;
            left: -33px;
            -o-transform: scale(1);
        }
        17% {
            -o-transform: scale(.5);
        }
        33% {
            top: -33px;
            left: 0;
            -o-transform: scale(1);
        }
        50% {
            -o-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: 33px;
            -o-transform: scale(1);
        }
        83% {
            -o-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: -33px;
            -o-transform: scale(1);
        }
    }

    @-ms-keyframes cssload-spin3 {
        0% {
            top: 33px;
            left: -33px;
            -ms-transform: scale(1);
        }
        17% {
            -ms-transform: scale(.5);
        }
        33% {
            top: -33px;
            left: 0;
            -ms-transform: scale(1);
        }
        50% {
            -ms-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: 33px;
            -ms-transform: scale(1);
        }
        83% {
            -ms-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: -33px;
            -ms-transform: scale(1);
        }
    }

    @-webkit-keyframes cssload-spin3 {
        0% {
            top: 33px;
            left: -33px;
            -webkit-transform: scale(1);
        }
        17% {
            -webkit-transform: scale(.5);
        }
        33% {
            top: -33px;
            left: 0;
            -webkit-transform: scale(1);
        }
        50% {
            -webkit-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: 33px;
            -webkit-transform: scale(1);
        }
        83% {
            -webkit-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: -33px;
            -webkit-transform: scale(1);
        }
    }

    @-moz-keyframes cssload-spin3 {
        0% {
            top: 33px;
            left: -33px;
            -moz-transform: scale(1);
        }
        17% {
            -moz-transform: scale(.5);
        }
        33% {
            top: -33px;
            left: 0;
            -moz-transform: scale(1);
        }
        50% {
            -moz-transform: scale(.5);
        }
        66% {
            top: 33px;
            left: 33px;
            -moz-transform: scale(1);
        }
        83% {
            -moz-transform: scale(.5);
        }
        100% {
            top: 33px;
            left: -33px;
            -moz-transform: scale(1);
        }
    }
    .center {
        margin-top:120px;
    }
</style>
<body>
<script type="text/javascript">
<#include "entrypoint.js">
</script>
<!--[if lt IE 8]>
<p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->
<div id="content">
    <div class="cssload-loading center">
        <i></i>
        <i></i>
        <i></i>
    </div>
</div>
</body>
</html>
