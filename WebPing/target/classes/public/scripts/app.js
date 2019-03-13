
var app = angular.module('todoapp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'angular-toArrayFilter',  //https://github.com/petebacondarwin/angular-toArrayFilter
    "chart.js"
   
   
]);





app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/list.html',
        controller: 'ListCtrl'
    }).when('/create', {
        templateUrl: 'views/create.html',
        controller: 'CreateCtrl'
    }).when('/chart', {
        templateUrl: 'views/chart.html',
        controller: 'ChartCtrl'
    }).when('/report', {
        templateUrl: 'views/report.html',
        controller: 'ReportCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});







app.controller('ListCtrl', function ($scope, $http, $location) {
    
	var sort="not set";

	  $scope.count = 0;
	  console.log($scope.count);
	  $scope.myFunc = function(device) {
	    $scope.count++;
	    console.log(device);
	    
	    $http.post('/pings', device).success(function (data) {
	    	console.log(data+' item attempted to update')
	       
	    }).error(function (data, status) {
	        console.log('Ping Post Error: ' + data)
	    })
	    
	  };
	  
	
	
	
	$http.get('/devices').success(function (data) {
        $scope.devices = data;
        sort = Object.keys($scope.devices[0])[3]; // 0:"id"  1:"title  2:"done"  3:"createdOn"  4:"inet"  5:"pingable"
        
        $scope.sortType = sort;  // set the default sort type
        console.log($scope.sortType);
        
    	$http.get('/deviceStats').success(function (data) {
    	    $scope.numDevices = data[0];
    	    $scope.disconnectedDevicesPercent = Math.round(data[1]* 100);
    	    $scope.connectedDevices = data[2];
    	    $scope.downtime = data[3];
    	    console.log(data);
    	}).error(function (data, status) {
    		$location.path();
    	    console.log($location.path() +' Get Error- ' + data)
    	})
        
        
        
    }).error(function (data, status) {
    	$location.path();
        console.log($location.path() +' Get Error- ' + data)
    })
 
        $scope.sortReverse  = false;  // set the default sort order
    	$scope.searchFish   = '';     // set the default search/filter term

    $scope.deviceStatusChanged = function (device) {
        
        $http.delete('/devices/'+ device.id).success(function (data) {
            console.log('item deleted');
            
            
          
        }).error(function (data, status) {
            console.log('Delete Error: ' + data)
        })
        $location.path('/');
        

        
    }
    

});



app.controller('CreateCtrl', function ($scope, $http, $location) {
    $scope.device = {
        done: false
        
        
    };

    //createTodo() called from create.html from ng-submit
    $scope.createDevice = function () {
        //console.log($scope.todo);
        
        //re-route to home page
        
      
        
        $http.post('/devices', $scope.device).success(function (data) {
            $location.path('/');
            $scope.$apply();
            console.log("item added");
        }).error(function (data, status) {
            console.log('Post Error= ' + data+ " ->"+status)
        })
        $location.path('/');
    }
});


function onlyUnique(value, index, self) { 
    return self.indexOf(value) === index;
}

app.controller('ChartCtrl', function ($scope,$location,$http) {
	
	

	    
			  $http.get('/graph/device.id').success(function (getData) {
		          
		        
		          console.log(getData[0]);
		          console.log(getData[1]);
				  $scope.line_labels = getData[1];
				  //$scope.line_series = ['Series A', 'Series B'];
				  $scope.line_data = getData[0];
				    
				  $scope.onClick = function (points, evt) {
				    console.log(points, evt);
				  };
				  $scope.datasetOverride = [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }];
				  $scope.line_options = {
				    scales: {
				      yAxes: [
				        {
				          id: 'y-axis-1',
				          type: 'linear',
				          display: true,
				          position: 'left'
				        },
				        {
				          id: 'y-axis-2',
				          type: 'linear',
				          display: true,
				          position: 'right'
				        }
				      ]
				    }
				  };
			  });
	    
	    	

});
