(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('CurrencyDetailController', CurrencyDetailController);

    CurrencyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Currency', 'Savings', 'Deposits', 'Funds', 'Stock'];

    function CurrencyDetailController($scope, $rootScope, $stateParams, entity, Currency, Savings, Deposits, Funds, Stock) {
        var vm = this;
        vm.currency = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:currencyUpdate', function(event, result) {
            vm.currency = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
