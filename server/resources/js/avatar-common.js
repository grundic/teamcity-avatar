var Avatar = {

  USERNAME_REGEX: /TeamCity user: (.*)(?="\);)/,

  currentUser: {},
  _cache: {},

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
      url += "?username='" + username + "'";
    }

    if (username in Avatar._cache) {
      callback($j.extend({"avatarUrl": Avatar._cache[username]}, param_hash));
    } else {
      BS.ajaxRequest(encodeURI(url), {
                onSuccess: function (transport) {
                  var avatarUrl = $j(transport.responseText).find("avatarUrl").text();
                  if (avatarUrl) {
                    Avatar._cache[username] = avatarUrl;
                    callback($j.extend({"avatarUrl": avatarUrl}, param_hash));
                  }
                }
              }
      );
    }
  },

  /*
   * Add avatar for current user to user panel.
   */
  addAvatarToUserPanel: function () {
    this._getAvatarUrl(Avatar.currentUser.extendedName, function (param_hash) {
      if ($j("#avatar-profile").length == 0) {
        $j("#beforeUserId").after('<img id="avatar-profile" class="avatar" src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
      } else {
        $j("#avatar-profile").attr("src", param_hash['avatarUrl']);
      }
    });
  },

  /*
   * Function for hacking standard showPopupNearElement.
   * We need to inject custom code in callback function,
   * to be able to modify div content with user avatar.
   * That's what this function do.
   */
  showPopupNearElementHack: function () {

    // we want to add our code after this line
    var searchString = "element.attr('data-popup', this._name);";
    // save current function
    var currentShowPopupNearElement = BS.Popup.prototype.showPopupNearElement.toString();
    // replace it, adding call to setting avatar function
    var hackedShowPopupNearElement = currentShowPopupNearElement.replace(searchString, searchString + '\n' + 'Avatar.addAvatarToPendingChanges();');

    // hack it!
    eval('BS.Popup.prototype.showPopupNearElement = ' + hackedShowPopupNearElement);
  },

  /*
   * This function is injected in callback of
   * popup window with pending changes.
   * Get username, check for avatar and add it.
   */
  addAvatarToPendingChanges: function () {
    var $this = this;
    $j('#groupedChanges>div>div>span').each(function () {
      var username = this.innerHTML;
      var element_id = "avatar-pending-" + $this._hashCode(username);

      if ($j("#" + element_id).length == 0) {
        $this._getAvatarUrl(username, function (param_hash) {
          $j(param_hash['this']).before('<img class="avatar" id="' + element_id + '" src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
        }, {"this": this});
      }
    });
  },

  addAvatarToPendingChangesDivTab: function () {
    var $this = this;

    $j($j('#changesTable tbody td.userName span').each(function () {
      var $span = this;
      var _onmouseover = $j(this).attr('onmouseover');
      var match = Avatar.USERNAME_REGEX.exec(_onmouseover);
      if (match) {
        var username = match[1];
        $this._getAvatarUrl(username, function (param_hash) {
          $j($span).before('<img class="avatar" src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
        });
      } else {
        console.log("[ERROR] Failed to match username: " + _onmouseover);
      }
    })
    );
  }
};