'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('enfant', {
                parent: 'entity',
                url: '/enfants',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.enfant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/enfant/enfants.html',
                        controller: 'EnfantController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('enfant');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('enfant.detail', {
                parent: 'entity',
                url: '/enfant/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.enfant.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/enfant/enfant-detail.html',
                        controller: 'EnfantDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('enfant');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Enfant', function($stateParams, Enfant) {
                        return Enfant.get({id : $stateParams.id});
                    }]
                }
            })
            .state('enfant.new', {
                parent: 'enfant',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/enfant/enfant-dialog.html',
                        controller: 'EnfantDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('enfant', null, { reload: true });
                    }, function() {
                        $state.go('enfant');
                    })
                }]
            })
            .state('enfant.edit', {
                parent: 'enfant',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/enfant/enfant-dialog.html',
                        controller: 'EnfantDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Enfant', function(Enfant) {
                                return Enfant.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('enfant', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
