'use strict';

describe('Controller Tests', function() {

    describe('Stock Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStock, MockCurrency, MockStockAudit, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStock = jasmine.createSpy('MockStock');
            MockCurrency = jasmine.createSpy('MockCurrency');
            MockStockAudit = jasmine.createSpy('MockStockAudit');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Stock': MockStock,
                'Currency': MockCurrency,
                'StockAudit': MockStockAudit,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("StockDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:stockUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
