angular.module('App')
    .controller('RoleController', ['$location', '$scope', '$rootScope', 'RoleService', 'ModalService', 'AlertService', 'FlashMessage', '$anchorScroll', '$filter',  
function RoleController($location, $scope, $rootScope, RoleService, ModalService, AlertService, FlashMessage,  $anchorScroll, $filter) {
	var self = this;
	$scope.role={id:null,rolename:'',roledesc:'',permissions:[{id:null,permissionname:'',permissiondesc:'',group:'',category:'',rights:''}]};
	self.roles=[];
	$scope.VALID_PATTERN = '^[A-Z0-9_]+$';
    $scope.currentPage = 1;  
    $scope.numPerPage = 5;  
    $scope.totalItems = 0;  
    $scope.noOfPages = 5;
    $scope.filteredList=[];
    $scope.filterParams = {
    		rolename: '',
    		roledesc: '',
            predicate: '',
            reverse: false
	};

    $scope.updateFilter = function(){
        var filtered;
        filtered = $filter('filter')(self.roles, {rolename:$scope.filterParams.rolename,roledesc:$scope.filterParams.roledesc});
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
    	$scope.filterParams.rolename = '';
    	$scope.filterParams.roledesc = '';
    };  

    $scope.paginate = function (value) {  
      var begin, end, index;  
      begin = ($scope.currentPage - 1) * $scope.numPerPage;  
      end = begin + $scope.numPerPage;  
      index = $scope.filteredList.indexOf(value);  
      return (begin <= index && index < end);  
    };  
	
    self.reset = function(){
    	$scope.role={id:null,rolename:'',roledesc:'',permissions:[{id:null,permissionname:'',permissiondesc:'',group:'',category:'',rights:''}]};
        $scope.myForm.$setPristine(); //reset Form
    };
    
    self.fetchAllRoles = function(){
    	RoleService.fetchAllRoles()
    	.then(
			function(d) {
				self.roles = d;
				$scope.updateFilter();
			},
			function(errResponse){
				AlertService.add('danger', 'Error while fetching Roles.');
			}
    	);
    };
    
    self.createRole = function(role){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Add Role',
                headerText: 'Add Role?',
                bodyText: {'Role Name':role.rolename,'Description':role.roledesc,'Permissions':role.permissions}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
        	RoleService.createRole(role)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllRoles();	
            		  AlertService.add('success', 'Role added successfully.');
	              },	
        		  function(errResponse){
	            	  if (errResponse.status==409){
	            		  AlertService.add('danger', 'Role Name '+ role.rolename +' already exists.');
	            	  } else{
	    				  AlertService.add('danger', 'Error while adding Role.');
	            	  }
	              }	
            );
        });
    	
    };
    
    self.updateRole = function(role, id){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Update Role',
                headerText: 'Update Role?',
                bodyText: {'Role Name':role.rolename,'Description':role.roledesc,'Permissions':role.permissions}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	        RoleService.updateRole(role, id)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllRoles();	
            		  AlertService.add('success', 'Role updated successfully.');
	              },	
        		  function(errResponse){
    				  AlertService.add('danger', 'Error while updating Role.');
	              }	
            );
        });
    };

    self.deleteRole = function(role){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete Role',
                headerText: 'Delete Role?',
                bodyText: {'Role Name':role.rolename,'Description':role.roledesc,'Permissions':role.permissions}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	    	RoleService.deleteRole(role.id)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllRoles();	
            		  AlertService.add('success', 'Role deleted successfully.');
	              },	
        		  function(errResponse){
    				  AlertService.add('danger', 'Error while deleting Role.');
	              }	
            );
        });
    };

    self.fetchAllRoles();

    self.submit = function() {
        if($scope.role.id==null){
            self.createRole($scope.role);
        }else{
        	self.updateRole($scope.role, $scope.role.id);
        }
    };
        
    self.edit = function(id){
        for(var i = 0; i < self.roles.length; i++){
            if(self.roles[i].id == id) {
            	$scope.role = angular.copy(self.roles[i]);
               break;
            }
        }
    };
        
    self.remove = function(role){
        self.deleteRole(role);
    };

    self.cancel = function(){
    	self.reset();
        self.fetchAllRoles();
    };
    
    self.checkItem = function (id) {
 	   var checked = false;
 	   if (angular.isArray($scope.role.permissions)){
	 	   for(var i=0; i < $scope.role.permissions.length; i++) {
	 		   if($scope.role.permissions[i].id == id) {
	 			   checked = true;
	 			   break;
	 		   }
	 	   }
 	   }
 	   return checked;
 	};

 	$scope.updateSelected = function (action, permission) {
    	if (action === 'add') {
    		if (angular.isArray($scope.role.permissions)){
    			var missing = true;
    			for(var i=0; i < $scope.role.permissions.length; i++) {
    				if($scope.role.permissions[i].id == permission.id) {
    					missing = false;
    					break;
    				}
    			}
    			if (missing) {
    				$scope.role.permissions.push(permission);
    			}
	  	   } else {
	  		   $scope.role.permissions=[];
	  		   $scope.role.permissions.push(permission);
	  	   }
    	}
    	if (action === 'remove') {
 	  	   if (angular.isArray($scope.role.permissions)){
	 	 	   for(var i=0; i < $scope.role.permissions.length; i++) {
	 	 		   if($scope.role.permissions[i].id == permission.id) {
	 	 			   $scope.role.permissions.splice(i,1); 
	 	 		   }
	 	 	   }
 	  	   }
    	}    	
  	};

  	$scope.toggleItem = function() {
		var action = (this.permission.selected ? 'add' : 'remove');
		$scope.updateSelected(action, this.permission);
  	};
  	
}]);