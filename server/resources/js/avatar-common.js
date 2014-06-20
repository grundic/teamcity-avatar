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
  _getAvatarUrl: function (parameters, callback, param_hash) {
    var url = window['base_uri'] + "/avatarAjax.html";
    url += "?" + $j.param(parameters);
    username = parameters["username"];
    if (username !== undefined && username in Avatar._cache) {
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
    console.log("This method should be implemented for each Teamcity version individually, because of differences in page makeup");
  },

  /*
   * Add hidden div with link to the image user's avatar and create handler for mouse move.
   */
  addBigAvatar: function (username, img_url, element) {
    var big_avatar_id = "big-avatar-" + this._hashCode(username);
    if ($j("#" + big_avatar_id).length == 0) {
      $j('body').append('<div id="big-avatar-container"><img id="' + big_avatar_id + '" class="big-avatar" src="' + img_url + '"></div>');
    } else {
      $j("#" + big_avatar_id).attr("src", img_url);
    }

    // add handlers for mousemove/mouseout events
    element.mousemove(function (event) {
      $j("#" + big_avatar_id).css({top: event.pageY + 15, left: event.pageX}).show();
    }).mouseout(function () {
              $j("#" + big_avatar_id).hide();
            });
  },

  /*
   * Function for hacking standard showPopupNearElement.
   * We need to inject custom code in callback function,
   * to be able to modify div content with user avatar.
   * That's what this function do.
   */
  showPopupNearElementHack: function () {
    // save current function
    var currentShowPopupNearElement = BS.Popup.prototype.showPopupNearElement.toString();
    // replace it, adding call to setting avatar function
    var hackedShowPopupNearElement = currentShowPopupNearElement.replace(
            /(\w+\.attr\(['"]data-popup['"]\s*,\s*this\._name\);)/,
            '$1\nAvatar.addAvatarToPendingChanges();'
    );

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
        $this._getAvatarUrl({"username":username}, function (param_hash) {
          $j(param_hash['this']).before('<img class="avatar" id="' + element_id + '" src="' + param_hash['avatarUrl'] + '">');
          $this.addBigAvatar(username, param_hash['avatarUrl'], $j("#" + element_id));
        }, {"this": this});
      }
    });
  },

  addAvatarToPendingChangesDivTab: function () {
    var $this = this;

    $j($j('#changesTable tbody td.userName span').each(function (i) {
      var $span = this;
      var _onmouseover = $j(this).attr('onmouseover');
      var match = Avatar.USERNAME_REGEX.exec(_onmouseover);

      if (match) {
        var username = match[1];
        var element_id = "avatar-pending-div-" + $this._hashCode(username) + "-" + i;

        $this._getAvatarUrl({"username":username}, function (param_hash) {
          $j($span).before('<img class="avatar" id="' + element_id + '" src="' + param_hash['avatarUrl'] + '">');
          $this.addBigAvatar(username, param_hash['avatarUrl'], $j("#" + element_id));
        });
      } else {
        console.log("[ERROR] Failed to match username: " + _onmouseover);
      }
    })
    );
  }
};