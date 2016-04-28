(function ($) {
    $('.js-restore').on('click', function (e) {
        e.preventDefault();
        var u = $('#u').val();
        $.post($(this).data('url'), {u: u}, function (data) {
            if (data.status) {
                alert('恢复成功!');
            }
        }, 'json');
    });
})(jQuery);