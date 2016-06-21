(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('SavingsDetailController', SavingsDetailController);

    SavingsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Savings', 'Currency', 'SavingsAudit', 'User'];

    function SavingsDetailController($scope, $rootScope, $stateParams, entity, Savings, Currency, SavingsAudit, User) {
        var vm = this;
        vm.savings = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:savingsUpdate', function(event, result) {
            vm.savings = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
