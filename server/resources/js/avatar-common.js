var Avatar = {

  USERNAME_REGEX: /TeamCity user: (.*)(?="\);)/,

  /*
   * Return hash code for string.
   */
  _hashCode: function (s) {
    return s.split("").reduce(function (a, b) {
      a = ((a << 5) - a) + b.charCodeAt(0);
      return a & a
    }, 0);
  },

  /*
   * Execute ajax request and get avatar url for username.
   */
  _getAvatarUrl: function (username, callback, param_hash) {
    var url = window['base_uri'] + "/avatarAjax.html";
    if (username) {
      url += "?username=" + username;
    }

    BS.ajaxRequest(encodeURI(url), {
              onSuccess: function (transport) {
                var avatarUrl = $j(transport.responseText).find("avatarUrl").text();
                if (avatarUrl) {
                  callback($j.extend({"avatarUrl": avatarUrl}, param_hash));
                }
              }
            }
    );

  }
};