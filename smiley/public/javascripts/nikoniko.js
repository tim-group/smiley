$(function() {

  var fromDate = "2013-09-30";

  $.get( "/smilies/" + fromDate, function(data) {
    $.each(data, function(name, smilies) {
      var row = $("<tr/>"), label = $("<td>" + name + "</td>");
      row.append(label);
      $(".nikoniko-individuals").append(row);
    });

//    alert("We got: " + JSON.stringify(data));
  });

});