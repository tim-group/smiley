$(function() {

  function lastMonday() {
    var lastWeek = new XDate().addWeeks(-1);
    return new XDate().setWeek(lastWeek.getWeek(), lastWeek.getFullYear());
  }

  var fromDate = lastMonday();

  var ratingSymbol = {
    "happy": "<img src='/assets/images/email-smile.png'/>",
    "neutral": "<img src='/assets/images/email-neutral.png'/>",
    "sad": "<img src='/assets/images/email-frown.png'/>"
  };


  function deriveReadableName(emailAddress) {
    var firstName = emailAddress.slice(0, emailAddress.indexOf("."));
    var lastName = emailAddress.slice(firstName.length + 1, emailAddress.indexOf("@"));
    return firstName.slice(0, 1).toUpperCase() + firstName.substr(1) + " " + lastName.slice(0, 1).toUpperCase();
  }

  function renderWeek(fromDate, smilies, row) {
    var startDate = new XDate(fromDate);
     for(i = 0; i < 5; i++) {
        var dateKey = startDate.toString("yyyy-MM-dd");
        var rating = smilies[dateKey];
        var td = $("<td class='sentiment'></td>");
        if (rating) {
          td.html(ratingSymbol[rating]);
        }
        row.append(td);
        startDate.addDays(1);
      }
  }

  function renderOnePersonsSentiment(data) {
    $(".nikoniko-individuals").empty();
    $.each(data, function(name, smilies) {
      var row = $("<tr/>"), label = $("<td class='name'>" + deriveReadableName(name) + "</td>"), startDate = new XDate(fromDate);
      row.append(label);

      renderWeek(startDate, smilies, row);
      startDate.addDays(7);
      row.append("<td class='weekend'/>");
      renderWeek(startDate, smilies, row);

      $(".nikoniko-individuals").append(row);

    });
  }

  function populateSentiments() {
    $.get( "/smilies/" + fromDate.toString("yyyy-MM-dd"), renderOnePersonsSentiment);
  }

  populateSentiments();
  window.setInterval(populateSentiments, 60000);

});