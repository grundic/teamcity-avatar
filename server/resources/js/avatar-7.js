/*
 * Add avatar for current user to user panel.
 */
Avatar.addAvatarToUserPanel = function () {
  this._getAvatarUrl(null, function (param_hash) {
    $j("#beforeUserId").after('<img src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
  });
};


/*
 * Function for hacking standard showPopupNearElement.
 * We need to inject custom code in callback function,
 * to be able to modify div content with user avatar.
 * That's what this function do.
 */
Avatar.showPopupNearElementHack = function () {
  // we want to add our code after this line
  var searchString = "element.attr('data-popup', this._name);";
  // save current function
  var currentShowPopupNearElement = BS.Popup.prototype.showPopupNearElement.toString();
  // replace it, adding call to setting avatar function
  var hackedShowPopupNearElement = currentShowPopupNearElement.replace(searchString, searchString + '\n' + 'Avatar.addAvatarToPendingChanges();');

  // hack it!
  eval('BS.Popup.prototype.showPopupNearElement = ' + hackedShowPopupNearElement);
};


/*
 * This function is injected in callback of
 * popup window with pending changes.
 * Get username, check for avatar and add it.
 */
Avatar.addAvatarToPendingChanges = function () {
  var $this = this;
  $j('#groupedChanges>div>div>span').each(function () {
    var username = this.innerHTML;
    var element_id = "avatar-pending-" + $this._hashCode(username);

    if ($j("#" + element_id).length == 0) {
      $this._getAvatarUrl(username, function (param_hash) {
        $j(param_hash['this']).before('<img id="' + element_id + '" src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
      }, {"this": this});
    }
  });
};

Avatar.addAvatarToPendingChangesDivTab = function () {
  var $this = this;

  $j($j('#changesTable tbody td.userName span').each(function () {
    var $span = this;
    var _onmouseover = $j(this).attr('onmouseover');
    var match = Avatar.USERNAME_REGEX.exec(_onmouseover);
    if (match) {
      var username = match[1];
      $this._getAvatarUrl(username, function (param_hash) {
        $j($span).before('<img src="' + param_hash['avatarUrl'] + '" height="18" width="18">');
      });
    } else {
      console.log("[ERROR] Failed to match username: " + _onmouseover);
    }
  })
  );
};

$j(document).ready(function () {
  Avatar.addAvatarToUserPanel();
  Avatar.showPopupNearElementHack();
  Avatar.addAvatarToPendingChangesDivTab();
});