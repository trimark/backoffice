angular.module('App')
    .controller('UserController', ['$location', '$scope', '$rootScope', 'UserService', 'ModalService', 'AlertService', 'FlashMessage', '$anchorScroll', '$filter',   
function UserController($location, $scope, $rootScope, UserService, ModalService, AlertService, FlashMessage, $anchorScroll, $filter) {
	var self = this;
    self.user={id:null,displayName:'',email:'',enabled:false,role:{id:null,rolename:'',roledesc:''}};
	self.users=[];
    $scope.currentPage = 1;  
    $scope.numPerPage = 5;  
    $scope.totalItems = 0;  
    $scope.noOfPages = 5;
    $scope.filteredList=[];
    $scope.filterParams = {
    		displayName: '',
    		email: '',
    		enabled: '',
    		role: '',
            predicate: '',
            reverse: false
	};

    $scope.updateFilter = function(){
        var filtered;
        filtered = $filter('filter')(self.users, {displayName:$scope.filterParams.displayName,email:$scope.filterParams.email,enabled:$scope.filterParams.enabled,role:{roledesc:$scope.filterParams.role}});
        filtered = $filter('orderBy')(filtered, $scope.filterParams.predicate, $scope.filterParams.reverse);
        $scope.filteredList = filtered;
	};
    
    $scope.$watchCollection('filterParams', function(newNames, oldNames) {
        $scope.updateFilter();
	});

    $scope.order = function (predicate) {  
    	$scope.filterParams.reverse = ($scope.filterParams.predicate === predicate) ? !$scope.filterParams.reverse : false;  
    	$scope.filterParams.predicate = predicate;  
    };  

    $scope.clearFilter = function () {  
    	$scope.filterParams.displayName = '';
    	$scope.filterParams.email = '';
    	$scope.filterParams.enabled = '';
    	$scope.filterParams.role = '';
    };  

    $scope.paginate = function (value) {  
      var begin, end, index;  
      begin = ($scope.currentPage - 1) * $scope.numPerPage;  
      end = begin + $scope.numPerPage;  
      index = $scope.filteredList.indexOf(value);  
      return (begin <= index && index < end);  
    };  
	
    self.reset = function(){
    	//self.user={id:null,displayName:'',email:''};
        self.user={id:null,displayName:'',email:'',enabled:false,role:{id:null,rolename:'',roledesc:''}};
        if ($scope.myForm) $scope.myForm.$setPristine(); //reset Form
    };
    
    self.fetchAllUsers = function(){
    	UserService.fetchAllUsers()
    	.then(
			function(d) {
				self.users = d;
				$scope.updateFilter();
			},
			function(errResponse){
				AlertService.add('danger', 'Error while fetching Users.');
			}
    	);
    };
    
    self.createUser = function(user){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Add User',
                headerText: 'Add User?',
                bodyText: {'User Name':user.displayName,'Email':user.email,'Enabled':user.enabled,'Role':user.role.roledesc}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	    	UserService.createUser(user)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllUsers();	
            		  AlertService.add('success', 'User added successfully.');
	              },	
	              function(errResponse){
	            	  if (errResponse.status==409){
	            		  AlertService.add('danger', 'User email '+ user.email +' already exists.');
	            	  } else{
	    				  AlertService.add('danger', 'Error while adding User.');
	            	  }
	              }	
            );
        });
    };
    
    self.updateUser = function(user, id){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Update User',
                headerText: 'Update User?',
                bodyText: {'User Name':user.displayName,'Email':user.email,'Enabled':user.enabled,'Role':user.role.roledesc}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	        UserService.updateUser(user, id)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllUsers();	
            		  AlertService.add('success', 'User updated successfully.');
	              },	
	              function(errResponse){
		               AlertService.add('danger', 'Error while updating User.');
	              }	
            );
        });
    };

    self.deleteUser = function(user){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete User',
                headerText: 'Delete User?',
                bodyText: {'User Name':user.displayName,'Email':user.email,'Enabled':user.enabled,'Role':user.role.roledesc}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	    	UserService.deleteUser(user.id)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllUsers();	
            		  AlertService.add('success', 'User deleted successfully.');
	              },	
	              function(errResponse){
		               AlertService.add('danger', 'Error while deleting User.');
	              }	
            );
        });
    };

    self.fetchAllUsers();

    self.submit = function() {
        if(self.user.id==null){
            self.createUser(self.user);
        }else{
        	self.updateUser(self.user, self.user.id);
        }
    };
        
    self.edit = function(id){
        for(var i = 0; i < self.users.length; i++){
            if(self.users[i].id == id) {
                self.user = angular.copy(self.users[i]);
               break;
            }
        }
    };
        
    self.remove = function(user){
        self.deleteUser(user);
    };

    self.cancel = function(){
        self.reset();
        self.fetchAllUsers();
    };
    
}]);