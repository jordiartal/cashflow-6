(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('DepositsDetailController', DepositsDetailController);

    DepositsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Deposits', 'Currency', 'User'];

    function DepositsDetailController($scope, $rootScope, $stateParams, entity, Deposits, Currency, User) {
        var vm = this;
        vm.deposits = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:depositsUpdate', function(event, result) {
            vm.deposits = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
