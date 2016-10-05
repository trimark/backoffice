angular.module('App')
    .controller('OrganizationController', ['$location', '$scope', '$rootScope', 'OrganizationService', 'ModalService', 'AlertService', 'FlashMessage', '$anchorScroll', '$filter',   
function OrganizationController($location, $scope, $rootScope, OrganizationService, ModalService, AlertService, FlashMessage, $anchorScroll, $filter) {
	var self = this;
    self.organization={id:null,organizationname:'',organizationdesc:'',parent:{id:null,organizationname:'',organizationdesc:''}};
    self.organizations=[];
    $scope.currentPage = 1;  
    $scope.numPerPage = 5;  
    $scope.totalItems = 0;  
    $scope.noOfPages = 5;
    $scope.filteredList=[];
    $scope.filterParams = {
    		organizationname: '',
    		organizationdesc: '',
            predicate: '',
            reverse: false
	};

    $scope.updateFilter = function(){
        var filtered;
        filtered = $filter('filter')(self.organizations, {organizationname:$scope.filterParams.organizationname,organizationdesc:$scope.filterParams.organizationdesc});
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
    	$scope.filterParams.organizationname = '';
    	$scope.filterParams.organizationdesc = '';
    };  

    $scope.paginate = function (value) {  
      var begin, end, index;  
      begin = ($scope.currentPage - 1) * $scope.numPerPage;  
      end = begin + $scope.numPerPage;  
      index = $scope.filteredList.indexOf(value);  
      return (begin <= index && index < end);  
    };  
	
    self.reset = function(){
        self.organization={id:null,organizationname:'',organizationdesc:'',parent:{id:null,organizationname:'',organizationdesc:''}};
        $scope.myForm.$setPristine(); //reset Form
    };
    
    self.fetchAllOrganizations = function(){
    	OrganizationService.fetchAllOrganizations()
    	.then(
			function(d) {
				self.organizations = d;
				$scope.updateFilter();
			},
			function(errResponse){
				AlertService.add('danger', 'Error while fetching Organizations.');
			}
    	);
    };
    
    self.createOrganization = function(organization){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Add Organization',
                headerText: 'Add Organization?',
                bodyText: {'Organization Name':organization.organizationname,'Description':organization.organizationdesc,'Parent Organization':organization.parent.organizationdesc}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	    	OrganizationService.createOrganization(organization)
              .then(
        		  function(d){
    				  self.reset();
            		  self.fetchAllOrganizations();	
            		  AlertService.add('success', 'Organization added successfully.');
	              },	
	              function(errResponse){
	            	  if (errResponse.status==409){
	            		  AlertService.add('danger', 'Organization Name '+ organization.organizationname +' already exists.');
	            	  } else{
	            		  AlertService.add('danger', 'Error while adding Organization.');
	            	  }
	              }	
            );
        });
    };
    
    self.updateOrganization = function(organization, id){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Update Organization',
                headerText: 'Update Organization?',
                bodyText: {'Organization Name':organization.organizationname,'Description':organization.organizationdesc,'Parent Organization':organization.parent.organizationdesc}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	        OrganizationService.updateOrganization(organization, id)
              .then(
        		  function(d){
    				  self.reset();
    				  self.fetchAllOrganizations();	
            		  AlertService.add('success', 'Organization updated successfully.');
	              },	
	              function(errResponse){
		               AlertService.add('danger', 'Error while updating Organization.');
	              }	
            );
        });
    };

    self.deleteOrganization = function(organization){
        var modalOptions = {
                closeButtonText: 'Cancel',
                actionButtonText: 'Delete Organization',
                headerText: 'Delete Organization?',
                bodyText: {'Organization Name':organization.organizationname,'Description':organization.organizationdesc,'Parent Organization':organization.parent.organizationdesc}
            };
    	
        ModalService.showModal({}, modalOptions).then(function (result) {
        	$anchorScroll();
	    	OrganizationService.deleteOrganization(organization.id)
              .then(
        		  function(d){
    				  self.reset();
    				  self.fetchAllOrganizations();	
            		  AlertService.add('success', 'Organization deleted successfully.');
	              },	
	              function(errResponse){
		               AlertService.add('danger', 'Error while deleting Organization.');
	              }	
            );
        });
    };

    self.fetchAllOrganizations();

    self.submit = function() {
        if(self.organization.id==null){
            self.createOrganization(self.organization);
        }else{
        	self.updateOrganization(self.organization, self.organization.id);
        }
    };
        
    self.edit = function(id){
        for(var i = 0; i < self.organizations.length; i++){
            if(self.organizations[i].id == id) {
                self.organization = angular.copy(self.organizations[i]);
                break;
            }
        }
    };
        
    self.remove = function(organization){
        self.deleteOrganization(organization);
    };

    self.cancel = function(){
    	self.reset();
    	self.fetchAllOrganizations();
    };
    
}]);