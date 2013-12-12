$(function() {

  function lastMonday() {
    var lastWeek = new XDate().addWeeks(-1);
    return new XDate().setWeek(lastWeek.getWeek(), lastWeek.getFullYear());
  }

  var ratingSymbol = {
    "happy": "<img src='/assets/images/email-smile.png'/>",
    "neutral": "<img src='/assets/images/email-neutral.png'/>",
    "sad": "<img src='/assets/images/email-frown.png'/>"
  };


  function deriveReadableName(emailAddress) {
    if (emailAddress.indexOf("@") === -1) {
      return emailAddress;
    }
    var firstName = emailAddress.slice(0, emailAddress.indexOf("."));
    var lastName = emailAddress.slice(firstName.length + 1, emailAddress.indexOf("@"));
    return firstName.slice(0, 1).toUpperCase() + firstName.substr(1) + " " + lastName.slice(0, 1).toUpperCase();
  }

  function renderWeek(week, smilies, row) {
      _.each(week, function(day) {
          var rating = smilies[day];
          var td = $("<td class='sentiment'></td>");
          if (rating && ratingSymbol[rating]) {
              td.html(ratingSymbol[rating]);
          }
          row.append(td);
      });
  }

  function renderEveryonesSentiment(data, weeks) {
    $(".nikoniko-individuals").empty();
    var names = Object.keys(data);
    names.sort();
    $.each(names, function(index, name) {
      var row = $("<tr/>"), label = $("<td class='name'>" + deriveReadableName(name) + "</td>");
      row.append(label);

        _.each(weeks, function(week) {
            renderWeek(week, data[name], row);
            row.append("<td class='weekend'/>");
        });

      $(".nikoniko-individuals").append(row);

    });

  }


  function renderTotalSentiment(data, dates) {
      var counts = totalSentiment(data, dates);
      var total = counts["happy"] + counts["sad"] + counts["neutral"];
      var happy = percentOf(counts["happy"], total);
      var sad = percentOf(counts["sad"], total);

      $(".happy-bar").height(parseInt(happy) + "%");
      $(".sad-bar").height(parseInt(sad) + "%")
  }

  function totalSentiment(data, datesToCalculateSentimentFor) {
      var counts = { "happy" : 0, "sad" : 0, "neutral" : 0};
      $.each(data, function(name, smilies) {
          $.each(smilies, function(date, sentiment) {
              if (_.contains(datesToCalculateSentimentFor, date)) {
                  counts[sentiment] += 1;
              }
          });
      });
      return counts;
  }

  function daysInWeeksFrom(fromDate, numWeeks) {
      var startDate = new XDate(fromDate);
      var dates = [];
      for(var i = 0; i < numWeeks; i++) {
          dates.push(daysInWeek(startDate));
          startDate.addDays(7);
      }
      return dates;
  }

  function daysInWeek(fromDate) {
      var startDate = new XDate(fromDate);
      var dates = [];
      for(var i = 0; i < 5; i++) {
          dates.push(startDate.toString("yyyy-MM-dd"));
          startDate.addDays(1);
      }
      return dates;
  }


  function percentOf(num, total) {
    return (num / total) * 100.0;
  }

  function populateSentiments() {
    // alert(location.search);
    var daysInWeeks = daysInWeeksFrom(lastMonday(), 2);

    $.get( "/smilies/" + daysInWeeks[0][0] + location.search, function(data) {
        renderEveryonesSentiment(data, daysInWeeks);
        renderTotalSentiment(data, _.flatten(daysInWeeks));
    });
  }

  populateSentiments();
  window.setInterval(populateSentiments, 60000);

});
