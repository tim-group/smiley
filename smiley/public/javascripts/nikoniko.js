$(

  $.get( "/smilies", function(data) {
    alert("We got: " + JSON.stringify(data));
  }));