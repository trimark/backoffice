angular.module('App')
    .controller('PermissionController', ['$location', '$scope', '$rootScope', 'PermissionService', 'ModalService', 'AlertService', 'FlashMessage', '$anchorScroll', '$filter',   
function PermissionController($location, $scope, $rootScope, PermissionService, ModalService, AlertService, FlashMessage, $anchorScroll, $filter) {
	var self = this;
	self.groups=[{'groupname':'System and Security'},
	             {'groupname':'Brands'},
	             {'groupname':'Scratchcards'}];
	self.categories=[{'groupname':'System and Security','catname':'User Management'},
	                 {'groupname':'System and Security','catname':'Role Management'},
	                 {'groupname':'System and Security','catname':'Permission Management'},
	                 {'groupname':'Brands','catname':'Brand A'},
	                 {'groupname':'Brands','catname':'Brand B'},
	                 {'groupname':'Brands','catname':'Brand C'},
	                 {'groupname':'Brands','catname':'Brand D'},
	                 {'groupname':'Brands','catname':'Brand E'},
	                 {'groupname':'Scratchcards','catname':'Model'},
	                 {'groupname':'Scratchcards','catname':'Lottery'},
	                 {'groupname':'Scratchcards','catname':'Stream'},
	                 {'groupname':'Scratchcards','catname':'Batch'}];
	self.rightslist=[{'rightsname':'Create'},
	                 {'rightsname':'Read'},
	                 {'rightsname':'Update'},
	                 {'rightsname':'Delete'}];
    self.permission={id:null,permissionname:'',permissiondesc:'',group:'',category:'',rights:''};
    self.permissions=[];
	$scope.VALID_PATTERN = '^[A-Z0-9_]+$';
    $scope.currentPage = 1;  
    $scope.numPerPage = 5;  
    $scope.totalItems = 0;  
    $scope.noOfPages = 5;
    $scope.filteredList=[];
    $scope.filterParams = {
    		permissionname: '',
    		permissiondesc: '',
            predicate: '',
            reverse: false
	};

    $scope.updateFilter = function(){
        var filtered;
        filtered = $filter('filter')(self.permissions, {permissionname:$scope.filterParams.permissionname,permissiondesc:$scope.filterParams.permissiondesc});
        filtered = $filter('orderBy')(filtered, $scope.filterParams.predicate, $scope.filterParams.reverse);
        $scope.filteredList = filtered;
	};
    
    $scope.$watchCollection('filterParams', function(newNames, oldNames) {
        $scope.updateFilter();
	});

    $scope.order = function (predicate) {  
    	$scope.filterParams.reverse = ($scope.filterParams.predicate === predicate) ? !$scope.filterParams.reverse : false;  
    	$scope.filterParams.predicate = predicate;  
    	//$scope.updateFilter();
    };  

    $scope.clearFilter = function () {  
    	$scope.filterParams.permissionname = '';
    	$scope.filterParams.permissiondesc = '';
    };  

    $scope.paginate = function (value) {  
      var begin, end, index;  
      begin = ($scope.currentPage - 1) * $scope.numPerPage;  
      end = begin + $scope.numPerPage;  
      index = $scope.filteredList.indexOf(value);  
      return (begin <= index && index < end);  
    };  
	
    self.reset = function(){
        self.permission={id:null,permissionname:'',permissiondesc:'',group:'',category:'',rights:''};
        $scope.myForm.$setPristine(); //reset Form
    };
    
    self.fetchAllPermissions = function(){
    	PermissionService.fetchAllPermissions()
    	.then(
			function(d) {
				self.permissions = d;
				$scope.updateFilter();
			},
			function(errResponse){
				AlertService.add('danger', 'Error while fetching Permissions.');
			}
    	);
    };
    
    self.createPermission = function(permission){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Add Permission',
                headerText: 'Add Permission?',
                bodyText: {'Permission Name':permission.permissionname,'Description':permission.permissiondesc,'Group':permission.group,'Category':permission.category,'Rights':permission.rights}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	    	PermissionService.createPermission(permission)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllPermissions();	
            		  AlertService.add('success', 'Permission added successfully.');
	              },	
	              function(errResponse){
	            	  if (errResponse.status==409){
	            		  AlertService.add('danger', 'Permission Name '+ permission.permissionname +' already exists.');
	            	  } else{
	            		  AlertService.add('danger', 'Error while adding Permission.');
	            	  }
	              }	
            );
        });
    };
    
    self.updatePermission = function(permission, id){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Update Permission',
                headerText: 'Update Permission?',
                bodyText: {'Permission Name':permission.permissionname,'Description':permission.permissiondesc,'Group':permission.group,'Category':permission.category,'Rights':permission.rights}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	        PermissionService.updatePermission(permission, id)
              .then(
        		  function(d){
    				  self.reset();
    				  self.fetchAllPermissions();	
            		  AlertService.add('success', 'Permission updated successfully.');
	              },	
	              function(errResponse){
		               AlertService.add('danger', 'Error while updating Permission.');
	              }	
            );
        });
    };

    self.deletePermission = function(permission){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete Permission',
                headerText: 'Delete Permission?',
                bodyText: {'Permission Name':permission.permissionname,'Description':permission.permissiondesc,'Group':permission.group,'Category':permission.category,'Rights':permission.rights}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	    	PermissionService.deletePermission(permission.id)
              .then(
        		  function(d){
    				  self.reset();
    				  self.fetchAllPermissions();	
            		  AlertService.add('success', 'Permission deleted successfully.');
	              },	
	              function(errResponse){
		               AlertService.add('danger', 'Error while deleting Permission.');
	              }	
            );
        });
    };

    self.fetchAllPermissions();

    self.submit = function() {
        if(self.permission.id==null){
            self.createPermission(self.permission);
        }else{
        	self.updatePermission(self.permission, self.permission.id);
        }
    };
        
    self.edit = function(id){
        for(var i = 0; i < self.permissions.length; i++){
            if(self.permissions[i].id == id) {
                self.permission = angular.copy(self.permissions[i]);
                break;
            }
        }
    };
        
    self.remove = function(permission){
        self.deletePermission(permission);
    };

    self.cancel = function(){
    	self.reset();
    	self.fetchAllPermissions();
    };
    
}]);