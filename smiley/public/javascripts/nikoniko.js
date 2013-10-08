$(function() {

  function lastMonday() {
    var lastWeek = new XDate().addWeeks(-1);
    return new XDate().setWeek(lastWeek.getWeek(), lastWeek.getFullYear());
  }

  var fromDate = lastMonday();

  $.get( "/smilies/" + fromDate.toString("yyyy-MM-dd"), function(data) {
    $.each(data, function(name, smilies) {
      var row = $("<tr/>"), label = $("<td>" + name + "</td>");
      row.append(label);
      $(".nikoniko-individuals").append(row);
    });

//    alert("We got: " + JSON.stringify(data));
  });

});