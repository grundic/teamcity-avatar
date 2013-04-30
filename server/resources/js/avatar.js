BS.ajaxRequest(window['base_uri'] + "/avatarAjax.html", {
          onSuccess: function (transport) {
            var avatarUrl = $j(transport.responseText).find("avatarUrl").text();
            var avatarUrlHtml = '<img src="' + avatarUrl + '" alt="Goofy" height="18" width="18">';
            $j("#beforeUserId").after(avatarUrlHtml);
          }
        }
);