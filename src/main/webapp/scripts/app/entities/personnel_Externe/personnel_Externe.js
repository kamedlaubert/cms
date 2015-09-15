'use strict';

angular.module('cmsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('personnel_Externe', {
                parent: 'entity',
                url: '/personnel_Externes',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.personnel_Externe.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/personnel_Externe/personnel_Externes.html',
                        controller: 'Personnel_ExterneController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('personnel_Externe');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('personnel_Externe.detail', {
                parent: 'entity',
                url: '/personnel_Externe/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'cmsApp.personnel_Externe.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/personnel_Externe/personnel_Externe-detail.html',
                        controller: 'Personnel_ExterneDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('personnel_Externe');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Personnel_Externe', function($stateParams, Personnel_Externe) {
                        return Personnel_Externe.get({id : $stateParams.id});
                    }]
                }
            })
            .state('personnel_Externe.new', {
                parent: 'personnel_Externe',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/personnel_Externe/personnel_Externe-dialog.html',
                        controller: 'Personnel_ExterneDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('personnel_Externe', null, { reload: true });
                    }, function() {
                        $state.go('personnel_Externe');
                    })
                }]
            })
            .state('personnel_Externe.edit', {
                parent: 'personnel_Externe',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/personnel_Externe/personnel_Externe-dialog.html',
                        controller: 'Personnel_ExterneDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Personnel_Externe', function(Personnel_Externe) {
                                return Personnel_Externe.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('personnel_Externe', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
