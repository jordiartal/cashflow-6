(function() {
    'use strict';

    angular
        .module('cashflow6App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('savings', {
            parent: 'entity',
            url: '/savings?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.savings.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/savings/savings.html',
                    controller: 'SavingsController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('savings');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('savings-detail', {
            parent: 'entity',
            url: '/savings/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cashflow6App.savings.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/savings/savings-detail.html',
                    controller: 'SavingsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('savings');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Savings', function($stateParams, Savings) {
                    return Savings.get({id : $stateParams.id});
                }]
            }
        })
        .state('savings.new', {
            parent: 'savings',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/savings/savings-dialog.html',
                    controller: 'SavingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                accountName: null,
                                actualValue: null,
                                initialValue: null,
                                initialDate: null,
                                newValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('savings', null, { reload: true });
                }, function() {
                    $state.go('savings');
                });
            }]
        })
        .state('savings.edit', {
            parent: 'savings',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/savings/savings-dialog.html',
                    controller: 'SavingsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Savings', function(Savings) {
                            return Savings.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('savings', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('savings.delete', {
            parent: 'savings',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/savings/savings-delete-dialog.html',
                    controller: 'SavingsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Savings', function(Savings) {
                            return Savings.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('savings', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('savings.history', {
            parent: 'entity',
            url : '/findAudit/{id}',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/savings-audit/history.html',
                    controller: 'SavingsAuditHistoryController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('savingsAudit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }],
                entity: ['$stateParams','SavingsAudit',function ($stateParams , SavingsAudit) {
                    return SavingsAudit.history({id : $stateParams.id});
                }]
            }
        })
    }

})();
