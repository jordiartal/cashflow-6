(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .controller('FundsDetailController', FundsDetailController);

    FundsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Funds', 'Currency', 'FundsAudit', 'User'];

    function FundsDetailController($scope, $rootScope, $stateParams, entity, Funds, Currency, FundsAudit, User) {
        var vm = this;
        vm.funds = entity;
        
        var unsubscribe = $rootScope.$on('cashflow6App:fundsUpdate', function(event, result) {
            vm.funds = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
