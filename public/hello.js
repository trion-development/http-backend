var app = angular.module('app', []);

app.controller('HelloCtrl', function ($http) {
    var o = this;
    $http.get('//localhost:8080/greeting').success(function (data) {
        o.greeting = data;
    });
});

(function () {
    var eventSource = new EventSource('/time');
    eventSource.addEventListener("message", function (event) {
        console.log(event.data);
    });
})();
