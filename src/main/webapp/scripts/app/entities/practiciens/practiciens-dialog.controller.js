'use strict';

angular.module('cmsApp').controller('PracticiensDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Practiciens', 'Consultation',
        function($scope, $stateParams, $modalInstance, entity, Practiciens, Consultation) {

        $scope.practiciens = entity;
        $scope.consultations = Consultation.query();
        $scope.load = function(id) {
            Practiciens.get({id : id}, function(result) {
                $scope.practiciens = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('cmsApp:practiciensUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.practiciens.id != null) {
                Practiciens.update($scope.practiciens, onSaveFinished);
            } else {
                Practiciens.save($scope.practiciens, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
