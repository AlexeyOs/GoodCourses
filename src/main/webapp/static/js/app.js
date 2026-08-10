var resume = {

    alert : function(message){
        alert(message);
    },

    moreProfiles : function() {
        var page = parseInt($('#profileContainer').attr('data-profile-number')) + 1;
        var total= parseInt($('#profileContainer').attr('data-profile-total'));
        if (page >= total) {
            $('#loadMoreIndicator').remove();
            $('#loadMoreContainer').remove();
            return;
        }
        var url = '/fragment/more?page=' + page;

        $('#loadMoreContainer').css('display', 'none');
        $('#loadMoreIndicator').css('display', 'block');
        $.ajax({
            url : url,
            success : function(data) {
                $('#loadMoreIndicator').css('display', 'none');
                $('#profileContainer').append(data);
                $('#profileContainer').attr('data-profile-number', page);
                if (page >= total-1) {
                    $('#loadMoreIndicator').remove();
                    $('#loadMoreContainer').remove();
                } else {
                    $('#loadMoreContainer').css('display', 'block');
                }
            },
            error : function(data) {
                $('#loadMoreIndicator').css('display', 'none');
                resume.alert('Error! Try again later...');
            }
        });
    }
};

resume.skills = {
    reindex : function() {
        $('#ui-block-container .skill-item').each(function(index) {
            var item = $(this);
            item.attr('id', 'ui-item-' + index);
            item.find('[name]').each(function() {
                this.name = this.name.replace(/items\[\d+\]/, 'items[' + index + ']');
            });
        });
    },

    add : function() {
        var index = $('#ui-block-container .skill-item').length;
        var template = $('#skill-row-template').html().replace(/__index__/g, index);
        $('#ui-block-container').append(template);
    }
};

$(function() {
    $(document).on('click', '.js-add-skill', function() {
        resume.skills.add();
    });
    $(document).on('click', '.js-remove-skill', function() {
        var item = $(this).closest('.skill-item');
        item.next('.skill-delim').remove();
        item.remove();
        resume.skills.reindex();
    });
});
