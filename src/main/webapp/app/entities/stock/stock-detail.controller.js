(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('StockDetailController', StockDetailController);

    StockDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Stock', 'Currency', 'StockAudit', 'User'];

    function StockDetailController($scope, $rootScope, $stateParams, entity, Stock, Currency, StockAudit, User) {
        var vm = this;
        vm.stock = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:stockUpdate', function(event, result) {
            vm.stock = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
