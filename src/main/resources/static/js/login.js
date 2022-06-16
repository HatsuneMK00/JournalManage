window.onload = function () {
    // 1.1 获取需要的标签
    var as = document.getElementsByClassName('content-header')[0].getElementsByTagName('a');
    var contents = document.getElementsByClassName('dom');

    // 1.2 遍历
    for (var i = 0; i < as.length; i++) {
        // 1.2.1 取出单个对象
        var a = as[i];
        a.id = i;

        // 1.2.2 监听鼠标的移动事件
        a.onclick = function () {
            // 让所有的a的class都清除
            for (var j = 0; j < as.length; j++) {
                as[j].className = '';
                contents[j].style.display = 'none';
            }

            // 设置当前a的class
            this.className = 'current';
            // 从contents数组中取出对应的标签
            contents[this.id].style.display = 'block';
        }
    }
};