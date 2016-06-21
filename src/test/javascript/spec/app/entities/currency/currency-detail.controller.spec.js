'use strict';

describe('Controller Tests', function() {

    describe('Currency Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCurrency, MockSavings, MockDeposits, MockFunds, MockStock;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCurrency = jasmine.createSpy('MockCurrency');
            MockSavings = jasmine.createSpy('MockSavings');
            MockDeposits = jasmine.createSpy('MockDeposits');
            MockFunds = jasmine.createSpy('MockFunds');
            MockStock = jasmine.createSpy('MockStock');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Currency': MockCurrency,
                'Savings': MockSavings,
                'Deposits': MockDeposits,
                'Funds': MockFunds,
                'Stock': MockStock
            };
            createController = function() {
                $injector.get('$controller')("CurrencyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:currencyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
