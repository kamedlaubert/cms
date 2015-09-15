'use strict';

angular.module('cmsApp').controller('Personnel_ExterneDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Personnel_Externe',
        function($scope, $stateParams, $modalInstance, entity, Personnel_Externe) {

        $scope.personnel_Externe = entity;
        $scope.load = function(id) {
            Personnel_Externe.get({id : id}, function(result) {
                $scope.personnel_Externe = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:personnel_ExterneUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.personnel_Externe.id != null) {
                Personnel_Externe.update($scope.personnel_Externe, onSaveFinished);
            } else {
                Personnel_Externe.save($scope.personnel_Externe, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
