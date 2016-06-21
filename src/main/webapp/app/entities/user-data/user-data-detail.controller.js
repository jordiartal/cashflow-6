(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('UserDataDetailController', UserDataDetailController);

    UserDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'UserData', 'User'];

    function UserDataDetailController($scope, $rootScope, $stateParams, entity, UserData, User) {
        var vm = this;
        vm.userData = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:userDataUpdate', function(event, result) {
            vm.userData = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
