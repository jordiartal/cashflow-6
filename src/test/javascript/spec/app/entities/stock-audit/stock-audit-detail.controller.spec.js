'use strict';

describe('Controller Tests', function() {

    describe('StockAudit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockStockAudit, MockStock;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockStockAudit = jasmine.createSpy('MockStockAudit');
            MockStock = jasmine.createSpy('MockStock');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'StockAudit': MockStockAudit,
                'Stock': MockStock
            };
            createController = function() {
                $injector.get('$controller')("StockAuditDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'cashflow6App:stockAuditUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
